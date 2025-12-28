package com.segnities007.yatte.domain.aggregate.category.usecase

import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import com.segnities007.yatte.domain.aggregate.category.repository.CategoryRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(id: CategoryId) {
        // TODO: Validate usage?
        categoryRepository.delete(id)
    }
}
