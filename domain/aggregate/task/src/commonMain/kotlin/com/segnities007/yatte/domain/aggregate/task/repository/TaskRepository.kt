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
    fun getTodayTasks(): Flow<List<Task>>

    fun getAllTasks(): Flow<List<Task>>

    suspend fun getById(id: TaskId): Task?

    suspend fun insert(task: Task)

    suspend fun update(task: Task)

    suspend fun delete(id: TaskId)

    /**
     * 期限切れタスクを削除
     *
     * 仕様:
     * - 対象は「1回限り」のタスクのみ（週次タスクは削除対象外）。
     * - 24時間後削除の起点は「作成時刻」ではなく「アラーム発火時刻」。
     *   これにより「通知が鳴ってから24時間」の仕様を満たす。
     */
    suspend fun deleteExpiredTasks()
}
