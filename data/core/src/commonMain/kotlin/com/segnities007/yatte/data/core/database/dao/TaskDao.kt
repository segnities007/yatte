package com.segnities007.yatte.data.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.segnities007.yatte.data.core.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * タスクDAO
 */
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: TaskEntity)

    @Update
    suspend fun update(entity: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    /**
     * アラーム発火時刻（tasks.alarm_triggered_at）を記録する。
     *
        * 仕様:
     * - 対象は **ONE_TIME タスクのみ**（週次タスクは自動削除しないため）
     * - この時刻は「24時間後自動削除」の起点として使用される
     */
    @Query("UPDATE tasks SET alarm_triggered_at = :triggeredAtMillis WHERE id = :taskId AND task_type = 'ONE_TIME'")
    suspend fun setAlarmTriggeredAtIfOneTimeTask(taskId: String, triggeredAtMillis: Long)

    /**
     * 期限切れの1回限りタスクを削除する。
     *
     * - alarm_triggered_at が設定済みで、thresholdMillis より古いものを削除
     * - ONE_TIME のみ対象（WEEKLY_LOOP は削除しない）
     */
    @Query("DELETE FROM tasks WHERE alarm_triggered_at IS NOT NULL AND alarm_triggered_at < :thresholdMillis AND task_type = 'ONE_TIME'")
    suspend fun deleteExpired(thresholdMillis: Long)
}
