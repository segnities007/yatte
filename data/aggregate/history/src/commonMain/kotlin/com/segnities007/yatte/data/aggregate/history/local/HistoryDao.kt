package com.segnities007.yatte.data.aggregate.history.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.segnities007.yatte.data.aggregate.history.local.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 履歴DAO
 */
@Dao
interface HistoryDao {
    @Query("SELECT * FROM histories ORDER BY completed_at DESC")
    fun getAll(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM histories WHERE completed_at >= :startOfDay AND completed_at < :endOfDay ORDER BY completed_at DESC")
    fun getByDate(startOfDay: Long, endOfDay: Long): Flow<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: HistoryEntity)

    @Query("DELETE FROM histories WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM histories")
    suspend fun deleteAll()
}
