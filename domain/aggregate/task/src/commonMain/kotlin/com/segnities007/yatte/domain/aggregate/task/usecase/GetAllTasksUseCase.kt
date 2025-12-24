package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * 全タスク一覧取得ユースケース
 */
class GetAllTasksUseCase(
    private val repository: TaskRepository,
) {
    /**
     * すべてのタスク一覧をFlowで取得する
     *
     * @return タスクのFlowリスト
     */
    operator fun invoke(): Flow<List<Task>> = repository.getAllTasks()
}
