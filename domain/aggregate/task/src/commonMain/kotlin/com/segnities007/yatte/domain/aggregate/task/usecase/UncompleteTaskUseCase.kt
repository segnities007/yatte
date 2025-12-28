package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.domain.core.error.EntityNotFoundError
import kotlinx.datetime.LocalDate

/**
 * タスク完了取り消しユースケース
 *
 * 指定した日付のタスク完了状態を取り消す（Undo機能用）。
 */
class UncompleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    /**
     * [taskId] のタスクの指定した [date] の完了状態を取り消す。
     *
     * @param taskId 完了取り消しするタスクのID
     * @param date 完了を取り消す日付
     */
    suspend operator fun invoke(taskId: TaskId, date: LocalDate): Result<Task> =
        runCatching {
            val task = taskRepository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val uncompletedTask = task.resetCompletion(date)
            taskRepository.update(uncompletedTask)
            uncompletedTask
        }
}
