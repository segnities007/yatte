package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.domain.core.error.EntityNotFoundError

/**
 * タスク完了ユースケース
 *
 * タスクを完了状態にする。
 *
 * **注意**: 履歴への追加は ViewModel で AddHistoryUseCase を別途呼び出すこと。
 * これにより循環依存を回避している。
 */
class CompleteTaskUseCase(
    private val taskRepository: TaskRepository,
) {
    /**
     * タスクを完了状態にする
     *
     * @param taskId 完了するタスクのID
     * @return 成功時は完了したTask、失敗時はエラー
     */
    suspend operator fun invoke(taskId: TaskId): Result<Task> =
        runCatching {
            val task = taskRepository.getById(taskId)
                ?: throw EntityNotFoundError("Task", taskId.value)

            val completedTask = task.complete()
            taskRepository.update(completedTask)
            completedTask
        }
}



