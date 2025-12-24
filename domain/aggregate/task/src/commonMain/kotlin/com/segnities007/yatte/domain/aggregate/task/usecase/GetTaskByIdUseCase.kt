package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
class GetTaskByIdUseCase(
    private val repository: TaskRepository,
) {
    /**
     * [taskId] でタスクを取得し、見つからない場合は `null` を返す。
     */
    suspend operator fun invoke(taskId: TaskId): Result<Task?> =
        runCatching {
            repository.getById(taskId)
        }
}
