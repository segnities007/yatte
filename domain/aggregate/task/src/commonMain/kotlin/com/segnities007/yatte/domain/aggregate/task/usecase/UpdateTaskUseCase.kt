package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
class UpdateTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * [task] を永続化し、更新する。
     */
    suspend operator fun invoke(task: Task): Result<Unit> =
        runCatching {
            repository.update(task)
        }
}
