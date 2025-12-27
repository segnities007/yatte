package com.segnities007.yatte.domain.aggregate.category.repository

import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<Category>>
    suspend fun getById(id: CategoryId): Category?
    suspend fun save(category: Category)
    suspend fun delete(id: CategoryId)
}
