package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.task.model.TaskId

/**
 * アラームキャンセルユースケース
 */
class CancelAlarmUseCase(
    private val repository: AlarmRepository,
) {
    /**
     * アラームをキャンセルする
     *
     * @param alarmId キャンセルするアラームのID
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(alarmId: AlarmId): Result<Unit> =
        runCatching {
            repository.cancel(alarmId)
        }

    /**
     * タスクに紐づくアラームをすべてキャンセルする
     *
     * @param taskId キャンセルするタスクのID
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend fun byTaskId(taskId: TaskId): Result<Unit> =
        runCatching {
            repository.cancelByTaskId(taskId)
        }
}
