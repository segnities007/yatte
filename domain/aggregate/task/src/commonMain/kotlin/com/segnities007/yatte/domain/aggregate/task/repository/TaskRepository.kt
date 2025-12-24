package com.segnities007.yatte.domain.aggregate.task.repository

import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.coroutines.flow.Flow

/**
 * タスクリポジトリのインターフェース
 *
 * Data層で実装される
 */
interface TaskRepository {
    /**
     * 今日のタスク一覧を取得
     */
    fun getTodayTasks(): Flow<List<Task>>

    /**
     * すべてのタスクを取得
     */
    fun getAllTasks(): Flow<List<Task>>

    /**
     * IDでタスクを取得
     */
    suspend fun getById(id: TaskId): Task?

    /**
     * タスクを作成
     */
    suspend fun insert(task: Task)

    /**
     * タスクを更新
     */
    suspend fun update(task: Task)

    /**
     * タスクを削除
     */
    suspend fun delete(id: TaskId)

    /**
     * 期限切れタスクを削除
     */
    suspend fun deleteExpiredTasks()
}
