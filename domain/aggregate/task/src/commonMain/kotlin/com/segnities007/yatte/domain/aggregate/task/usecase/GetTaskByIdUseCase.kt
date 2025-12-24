package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository

/**
 * タスク取得ユースケース
 */
class GetTaskByIdUseCase(
    private val repository: TaskRepository,
) {
    /**
     * IDでタスクを取得する
     *
     * @param taskId 取得するタスクのID
     * @return 成功時はTask（見つからない場合はnull）、失敗時はエラー
     */
    suspend operator fun invoke(taskId: TaskId): Result<Task?> =
        runCatching {
            repository.getById(taskId)
        }
}
