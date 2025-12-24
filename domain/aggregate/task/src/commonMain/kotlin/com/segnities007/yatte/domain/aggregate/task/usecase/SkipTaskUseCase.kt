package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.domain.core.error.EntityNotFoundError
import kotlinx.datetime.LocalDate

/**
 * 週次タスクスキップユースケース
 */
class SkipTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * 週次タスクを指定日までスキップする
     *
     * @param taskId スキップするタスクのID
     * @param until スキップする期限日
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(taskId: TaskId, until: LocalDate): Result<Unit> =
        runCatching {
            val task = repository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val skippedTask = task.skip(until)
            repository.update(skippedTask)
        }

    /**
     * スキップを解除する
     *
     * @param taskId スキップを解除するタスクのID
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend fun cancel(taskId: TaskId): Result<Unit> =
        runCatching {
            val task = repository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val unskippedTask = task.cancelSkip()
            repository.update(unskippedTask)
        }
}

