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

    @Query("DELETE FROM tasks WHERE alarm_triggered_at IS NOT NULL AND alarm_triggered_at < :thresholdMillis AND task_type = 'ONE_TIME'")
    suspend fun deleteExpired(thresholdMillis: Long)
}
