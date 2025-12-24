package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
class DeleteTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * [taskId] のタスクを削除する。
     */
    suspend operator fun invoke(taskId: TaskId): Result<Unit> =
        runCatching {
            repository.delete(taskId)
        }
}
