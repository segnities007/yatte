package com.segnities007.yatte.platform.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.alarm.usecase.ScheduleAlarmUseCase
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.usecase.AddHistoryUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.GetSettingsUseCase
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.domain.aggregate.task.usecase.CompleteTaskUseCase
import com.segnities007.yatte.platform.AndroidKoinBootstrapper
import com.segnities007.yatte.platform.notification.AlarmNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.context.GlobalContext
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * OSアラームの発火入口。
 *
 * 仕様:
 * - 発火したら必ずDBへ「発火済み」を記録する（24h後削除の起点）
 * - 通知権限がある場合は通知を表示する
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (intent.action != AlarmConstants.ACTION_ALARM_FIRED &&
            intent.action != AlarmConstants.ACTION_ALARM_EXTEND &&
            intent.action != AlarmConstants.ACTION_ALARM_COMPLETE
        ) {
            return
        }

        AndroidKoinBootstrapper.ensureStarted(context)

        val alarmIdValue = getAlarmIdFromIntent(intent) ?: return
        val alarmId = AlarmId(alarmIdValue)

        val goAsync = goAsync()

        val koin = GlobalContext.get()
        val alarmRepository = koin.get<AlarmRepository>()
        val taskRepository = koin.get<TaskRepository>()
        val scheduleAlarmUseCase = koin.get<ScheduleAlarmUseCase>()

        val completeTaskUseCase = koin.get<CompleteTaskUseCase>()
        val addHistoryUseCase = koin.get<AddHistoryUseCase>()
        val getSettingsUseCase = koin.get<GetSettingsUseCase>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (intent.action == AlarmConstants.ACTION_ALARM_EXTEND) {
                    val taskIdStr = getTaskIdFromIntent(intent) ?: return@launch
                    val taskId = TaskId(taskIdStr)
                    handleExtendAction(taskId, scheduleAlarmUseCase, getSettingsUseCase, context)
                } else if (intent.action == AlarmConstants.ACTION_ALARM_COMPLETE) {
                    val taskIdStr = getTaskIdFromIntent(intent) ?: return@launch
                    val taskId = TaskId(taskIdStr)
                    handleCompleteAction(taskId, completeTaskUseCase, addHistoryUseCase, context)
                } else {
                    handleAlarmFired(
                        alarmId,
                        alarmRepository,
                        taskRepository,
                        scheduleAlarmUseCase,
                        getSettingsUseCase,
                        context,
                    )
                }
            } finally {
                goAsync.finish()
            }
        }
    }

    private fun getTaskIdFromIntent(intent: Intent): String? = intent.getStringExtra(AlarmConstants.EXTRA_TASK_ID)

    private fun getAlarmIdFromIntent(intent: Intent): String? = intent.getStringExtra(AlarmConstants.EXTRA_ALARM_ID)

    private suspend fun handleExtendAction(
        taskId: TaskId,
        scheduleAlarmUseCase: ScheduleAlarmUseCase,
        getSettingsUseCase: GetSettingsUseCase,
        context: Context,
    ) {
        val settings = getSettingsUseCase().first()
        val snoozeDuration = settings.snoozeDuration

        // Cancel notification
        NotificationManagerCompat
            .from(context)
            .cancelAll()

        // Reschedule snoozeDuration mins later
        val newAlarm =
            Alarm(
                id =
                    AlarmId
                        .generate(),
                taskId = taskId,
                scheduledAt =
                    currentLocalDateTime()
                        .toInstant(
                            TimeZone.currentSystemDefault(),
                        ).plus(snoozeDuration.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
                // Schedule time isn't strictly used for one-off but for reference
                notifyAt =
                    currentLocalDateTime()
                        .toInstant(
                            TimeZone.currentSystemDefault(),
                        ).plus(snoozeDuration.minutes)
                        .toLocalDateTime(TimeZone.currentSystemDefault()),
            )
        scheduleAlarmUseCase(newAlarm)
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun handleCompleteAction(
        taskId: TaskId,
        completeTaskUseCase: CompleteTaskUseCase,
        addHistoryUseCase: AddHistoryUseCase,
        context: Context,
    ) {
        // Cancel notification
        NotificationManagerCompat
            .from(context)
            .cancelAll()

        // Complete task
        // Complete task
        val now = currentLocalDateTime()
        val today = now.date
        completeTaskUseCase(taskId, today)
            .onSuccess { completedTask ->
                // 履歴に追加
                val history =
                    History(
                        id = HistoryId(Uuid.random().toString()),
                        taskId = completedTask.id,
                        title = completedTask.title,
                        completedAt = now,
                    )
                addHistoryUseCase(history)
            }
    }

    private suspend fun handleAlarmFired(
        alarmId: AlarmId,
        alarmRepository: AlarmRepository,
        taskRepository: TaskRepository,
        scheduleAlarmUseCase: ScheduleAlarmUseCase,
        getSettingsUseCase: GetSettingsUseCase,
        context: Context,
    ) {
        val alarm = alarmRepository.getById(alarmId)
        val taskId = alarm?.taskId ?: return

        // 発火記録（24h削除の起点）
        alarmRepository.markAsTriggered(alarmId)

        // 通知（権限未許可なら表示しない）
        val task = taskRepository.getById(TaskId(taskId.value))
        val title = task?.title ?: taskId.value

        // 設定取得
        // 設定取得
        val settings = getSettingsUseCase().first()
        // アラーム固有の音があればそれを使用、なければ設定のカスタム音、それもなければnull（デフォルト）
        val soundUri = alarm.soundUri ?: settings.customSoundUri
        val isSoundEnabled = settings.notificationSound
        val isVibrationEnabled = settings.notificationVibration

        AlarmNotification.show(
            context = context,
            title = "やって: $title",
            content = "タスクの時間です",
            soundUri = soundUri,
            isSoundEnabled = isSoundEnabled,
            isVibrationEnabled = isVibrationEnabled,
            snoozeDuration = settings.snoozeDuration,
            vibrationPattern = settings.vibrationPattern,
            alarmId = alarmId.value,
            taskId = taskId.value,
        )

        // 週次タスクなら次回分を再スケジュール
        if (task != null && task.taskType == TaskType.WEEKLY_LOOP) {
            val now = currentLocalDateTime()
            val next = nextOccurrence(now, task.time, task.weekDays)
            val nextAlarm =
                Alarm(
                    id =
                        AlarmId
                            .generate(),
                    taskId = task.id,
                    scheduledAt = next,
                    notifyAt = subtractMinutes(next, task.minutesBefore),
                )
            scheduleAlarmUseCase(nextAlarm)
        }
    }
}

private fun currentLocalDateTime(): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    return Clock.System.now().toLocalDateTime(tz)
}

private fun subtractMinutes(
    dateTime: LocalDateTime,
    minutes: Int,
): LocalDateTime {
    val tz = TimeZone.currentSystemDefault()
    val instant = dateTime.toInstant(tz)
    return (instant - minutes.minutes).toLocalDateTime(tz)
}

private fun nextOccurrence(
    now: LocalDateTime,
    time: LocalTime,
    weekDays: List<DayOfWeek>,
): LocalDateTime {
    val today = now.date
    for (i in 0..6) {
        val date = today.plus(DatePeriod(days = i))
        if (!weekDays.contains(date.dayOfWeek)) continue

        val candidate = LocalDateTime(date, time)
        if (candidate >= now) return candidate
    }

    // 念のため（通常ここには来ない）
    val fallback = today.plus(DatePeriod(days = 7))
    return LocalDateTime(fallback, time)
}
