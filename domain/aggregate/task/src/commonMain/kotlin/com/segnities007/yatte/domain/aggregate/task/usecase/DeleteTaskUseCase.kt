package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository

/**
 * タスク削除ユースケース
 */
class DeleteTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * タスクを削除する
     *
     * @param taskId 削除するタスクのID
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(taskId: TaskId): Result<Unit> =
        runCatching {
            repository.delete(taskId)
        }
}
