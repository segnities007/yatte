package com.segnities007.yatte.data.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.segnities007.yatte.data.core.database.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow

/**
 * アラームDAO
 */
@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms WHERE is_triggered = 0 ORDER BY notify_at ASC")
    fun getScheduledAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getById(id: String): AlarmEntity?

    @Query("SELECT * FROM alarms WHERE task_id = :taskId AND is_triggered = 0")
    suspend fun getByTaskId(taskId: String): AlarmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AlarmEntity)

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM alarms WHERE task_id = :taskId")
    suspend fun deleteByTaskId(taskId: String)

    @Query("UPDATE alarms SET is_triggered = 1 WHERE id = :id")
    suspend fun markTriggered(id: String)

    @Query("DELETE FROM alarms WHERE is_triggered = 1")
    suspend fun deleteTriggered()
}
