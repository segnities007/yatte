package com.segnities007.yatte.platform.alarm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.platform.AndroidKoinBootstrapper
import com.segnities007.yatte.platform.notification.AlarmNotification
import org.koin.core.context.GlobalContext

/**
 * アラーム権限等で AlarmManager が使えない場合のフォールバック通知。
 *
 * 仕様:
 * - 実行時に必ずDBへ「発火済み」を記録する
 * - 通知権限がある場合は通知を表示する
 */
class AlarmNotifyWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        AndroidKoinBootstrapper.ensureStarted(applicationContext)

        val alarmIdValue = inputData.getString(AlarmConstants.EXTRA_ALARM_ID) ?: return Result.failure()
        val alarmId = AlarmId(alarmIdValue)

        val koin = GlobalContext.get()
        val alarmRepository = koin.get<AlarmRepository>()
        val taskRepository = koin.get<TaskRepository>()

        val alarm = alarmRepository.getById(alarmId)
        val taskId = alarm?.taskId
        val task = taskId?.let { taskRepository.getById(TaskId(it.value)) }
        val title = task?.title ?: taskId?.value ?: "Task"
        runCatching {
            alarmRepository.markAsTriggered(alarmId)
        }

        AlarmNotification.show(
            context = applicationContext,
            title = "やって: $title",
            content = "タスクの時間です",
        )

        return Result.success()
    }
}
