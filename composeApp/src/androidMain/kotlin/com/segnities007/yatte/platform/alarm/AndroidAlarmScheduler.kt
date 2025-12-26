package com.segnities007.yatte.platform.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import kotlinx.datetime.toInstant
import java.util.concurrent.TimeUnit

/**
 * Android用AlarmScheduler。
 *
 * 仕様:
 * - 基本は AlarmManager（アラーム）で notifyAt に発火させる
 * - 端末/OS制約で「正確なアラームをスケジュールできない」場合は WorkManager（通知）にフォールバック
 */
class AndroidAlarmScheduler(
    private val context: Context,
) : AlarmScheduler {
    override suspend fun schedule(alarm: Alarm) {
        val notifyAtMillis = alarm.notifyAt.toEpochMillis()

        if (canUseExactAlarm()) {
            scheduleWithAlarmManager(alarm, notifyAtMillis)
            return
        }

        // フォールバック: WorkManagerで通知
        scheduleWithWorkManager(alarm, notifyAtMillis)
    }

    override suspend fun cancel(alarmId: AlarmId) {
        cancelAlarmManager(alarmId)
        cancelWorkManager(alarmId)
    }

    private fun scheduleWithAlarmManager(
        alarm: Alarm,
        triggerAtMillis: Long,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(alarm)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent,
        )
    }

    private fun cancelAlarmManager(alarmId: AlarmId) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = buildPendingIntent(alarmId)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun scheduleWithWorkManager(
        alarm: Alarm,
        triggerAtMillis: Long,
    ) {
        val now = System.currentTimeMillis()
        val delayMillis = (triggerAtMillis - now).coerceAtLeast(0)

        val inputData =
            Data
                .Builder()
                .putString(AlarmConstants.EXTRA_ALARM_ID, alarm.id.value)
                .build()

        val request =
            OneTimeWorkRequestBuilder<AlarmNotifyWorker>()
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(workTag(alarm.id))
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(uniqueWorkName(alarm.id), ExistingWorkPolicy.REPLACE, request)
    }

    private fun cancelWorkManager(alarmId: AlarmId) {
        WorkManager
            .getInstance(context)
            .cancelUniqueWork(uniqueWorkName(alarmId))
    }

    private fun buildPendingIntent(alarm: Alarm): PendingIntent = buildPendingIntent(alarm.id, alarm.taskId.value)

    private fun buildPendingIntent(
        alarmId: AlarmId,
        taskTitle: String? = null,
    ): PendingIntent {
        val intent =
            Intent(context, AlarmReceiver::class.java)
                .setAction(AlarmConstants.ACTION_ALARM_FIRED)
                .putExtra(AlarmConstants.EXTRA_ALARM_ID, alarmId.value)
                .putExtra(AlarmConstants.EXTRA_TASK_TITLE, taskTitle)

        val flags =
            PendingIntent.FLAG_UPDATE_CURRENT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        return PendingIntent.getBroadcast(
            context,
            alarmId.value.hashCode(),
            intent,
            flags,
        )
    }

    private fun canUseExactAlarm(): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun uniqueWorkName(alarmId: AlarmId): String = "alarm_notify_${alarmId.value}"

    private fun workTag(alarmId: AlarmId): String = "alarm_${alarmId.value}"
}

private fun kotlinx.datetime.LocalDateTime.toEpochMillis(): Long {
    val tz = kotlinx.datetime.TimeZone.currentSystemDefault()
    return this.toInstant(tz).toEpochMilliseconds()
}
