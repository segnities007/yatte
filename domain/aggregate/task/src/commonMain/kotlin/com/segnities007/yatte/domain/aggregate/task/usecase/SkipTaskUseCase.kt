package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.domain.core.error.EntityNotFoundError
import kotlinx.datetime.LocalDate
class SkipTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * [taskId] の週次タスクを [until] までスキップする。
     */
    suspend operator fun invoke(taskId: TaskId, until: LocalDate): Result<Unit> =
        runCatching {
            val task = repository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val skippedTask = task.skip(until)
            repository.update(skippedTask)
        }

    /**
     * [taskId] のスキップを解除する。
     */
    suspend fun cancel(taskId: TaskId): Result<Unit> =
        runCatching {
            val task = repository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val unskippedTask = task.cancelSkip()
            repository.update(unskippedTask)
        }
}

