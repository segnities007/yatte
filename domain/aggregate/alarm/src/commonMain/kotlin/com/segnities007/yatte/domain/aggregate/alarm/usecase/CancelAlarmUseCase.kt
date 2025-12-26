package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
class CancelAlarmUseCase(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler,
) {
    /**
     * [alarmId] のアラームをキャンセルする。
     */
    suspend operator fun invoke(alarmId: AlarmId): Result<Unit> =
        runCatching {
            repository.cancel(alarmId)
            scheduler.cancel(alarmId)
        }

    /**
     * [taskId] に紐づくアラームをすべてキャンセルする。
     */
    suspend fun byTaskId(taskId: TaskId): Result<Unit> =
        runCatching {
            val alarm = repository.getByTaskId(taskId)
            repository.cancelByTaskId(taskId)

            // OS側のキャンセルは「見つかった1件」を対象にする（未発火は基本1件想定）
            alarm?.let { scheduler.cancel(it.id) }
        }
}
