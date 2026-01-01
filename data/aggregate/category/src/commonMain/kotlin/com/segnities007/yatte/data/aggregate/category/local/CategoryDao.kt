package com.segnities007.yatte.data.aggregate.category.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.segnities007.yatte.data.aggregate.category.local.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * カテゴリのDAO
 */
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY created_at DESC")
    fun getAll(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getById(id: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun delete(id: String)
}
