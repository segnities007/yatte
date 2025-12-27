package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.domain.core.error.EntityNotFoundError
import kotlinx.datetime.LocalDate

/**
 * タスク完了ユースケース
 *
 * 指定した日付でタスクを完了状態にする。
 *
 * 注意:
 * - 履歴への追加は ViewModel で AddHistoryUseCase を別途呼び出すこと。
 * これにより循環依存を回避している。
 */
class CompleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    /**
     * [taskId] のタスクを指定した [date] で完了状態にし、更新後のTaskを返す。
     *
     * @param taskId 完了するタスクのID
     * @param date 完了する日付
     */
    suspend operator fun invoke(taskId: TaskId, date: LocalDate): Result<Task> =
        runCatching {
            val task = taskRepository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val completedTask = task.complete(date)
            taskRepository.update(completedTask)
            completedTask
        }
}
