package com.segnities007.yatte.domain.aggregate.category.usecase

import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllCategoriesUseCase(
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getAll()
    }
}
