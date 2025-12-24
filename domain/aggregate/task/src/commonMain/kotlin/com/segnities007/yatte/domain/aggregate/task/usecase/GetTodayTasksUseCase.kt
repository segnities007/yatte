package com.segnities007.yatte.domain.aggregate.task.usecase

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * 今日のタスク一覧取得ユースケース
 */
class GetTodayTasksUseCase(
    private val repository: TaskRepository,
) {
    /**
     * 今日のタスク一覧をFlowで取得する
     *
     * @return タスクのFlowリスト
     */
    operator fun invoke(): Flow<List<Task>> = repository.getTodayTasks()
}
