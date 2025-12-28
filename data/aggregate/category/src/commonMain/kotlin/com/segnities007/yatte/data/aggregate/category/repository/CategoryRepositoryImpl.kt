package com.segnities007.yatte.data.aggregate.category.repository

import com.segnities007.yatte.data.core.database.dao.CategoryDao
import com.segnities007.yatte.data.core.database.entity.CategoryEntity
import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import com.segnities007.yatte.domain.aggregate.category.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
) : CategoryRepository {
    override fun getAll(): Flow<List<Category>> =
        categoryDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getById(id: CategoryId): Category? =
        categoryDao.getById(id.value)?.toDomain()

    override suspend fun save(category: Category) {
        categoryDao.insert(category.toEntity())
    }

    override suspend fun delete(id: CategoryId) {
        categoryDao.delete(id.value)
    }

    private fun CategoryEntity.toDomain(): Category =
        Category(
            id = CategoryId(id),
            name = name,
            color = CategoryColor.fromHex(colorHex),
            createdAt = createdAt,
        )

    private fun Category.toEntity(): CategoryEntity =
        CategoryEntity(
            id = id.value,
            name = name,
            colorHex = color.hex,
            createdAt = createdAt,
        )
}

