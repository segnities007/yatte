package com.segnities007.yatte.domain.aggregate.category.usecase

import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.repository.CategoryRepository

class AddCategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(category: Category) {
        categoryRepository.save(category)
    }
}
