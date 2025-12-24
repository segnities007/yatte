package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository

/**
 * タスク更新ユースケース
 */
class UpdateTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * タスクを更新する
     *
     * @param task 更新するタスク
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(task: Task): Result<Unit> =
        runCatching {
            repository.update(task)
        }
}
