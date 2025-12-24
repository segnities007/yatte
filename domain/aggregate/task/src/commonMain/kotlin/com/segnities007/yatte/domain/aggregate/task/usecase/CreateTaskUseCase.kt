package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository

/**
 * タスク作成ユースケース
 */
class CreateTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * 新しいタスクを作成する
     *
     * @param task 作成するタスク
     * @return 作成されたタスクのID
     */
    suspend operator fun invoke(task: Task): Result<TaskId> =
        runCatching {
            repository.insert(task)
            task.id
        }
}
