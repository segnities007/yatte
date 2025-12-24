package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
class CancelAlarmUseCase(
    private val repository: AlarmRepository,
) {
    /**
     * [alarmId] のアラームをキャンセルする。
     */
    suspend operator fun invoke(alarmId: AlarmId): Result<Unit> =
        runCatching {
            repository.cancel(alarmId)
        }

    /**
     * [taskId] に紐づくアラームをすべてキャンセルする。
     */
    suspend fun byTaskId(taskId: TaskId): Result<Unit> =
        runCatching {
            repository.cancelByTaskId(taskId)
        }
}
