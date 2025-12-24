package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
class CreateTaskUseCase(
    private val repository: TaskRepository,
) {
    /**
     * [task] を作成し、作成したタスクのIDを返す。
     */
    suspend operator fun invoke(task: Task): Result<TaskId> =
        runCatching {
            repository.insert(task)
            task.id
        }
}
