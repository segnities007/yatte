package com.segnities007.yatte.presentation.feature.category

import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor

/**
 * カテゴリ管理画面のUI状態
 */
data class CategoryState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isAddDialogVisible: Boolean = false,
    val newCategoryName: String = "",
    val selectedColor: CategoryColor = CategoryColor.BLUE,
)

/**
 * カテゴリ管理画面のインテント
 */
sealed interface CategoryIntent {
    data object LoadCategories : CategoryIntent
    data object ShowAddDialog : CategoryIntent
    data object DismissAddDialog : CategoryIntent
    data class UpdateNewCategoryName(val name: String) : CategoryIntent
    data class UpdateSelectedColor(val color: CategoryColor) : CategoryIntent
    data object AddCategory : CategoryIntent
    data class DeleteCategory(val category: Category) : CategoryIntent
}

/**
 * カテゴリ管理画面のイベント
 */
sealed interface CategoryEvent {
    data class ShowError(val message: String) : CategoryEvent
    data object CategoryAdded : CategoryEvent
    data object CategoryDeleted : CategoryEvent
}
