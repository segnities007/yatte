package com.segnities007.yatte.presentation.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.domain.aggregate.category.usecase.AddCategoryUseCase
import com.segnities007.yatte.domain.aggregate.category.usecase.DeleteCategoryUseCase
import com.segnities007.yatte.domain.aggregate.category.usecase.GetAllCategoriesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryState())
    val state: StateFlow<CategoryState> = _state.asStateFlow()

    private val _events = Channel<CategoryEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadCategories()
    }

    fun onIntent(intent: CategoryIntent) {
        when (intent) {
            is CategoryIntent.LoadCategories -> loadCategories()
            is CategoryIntent.ShowAddDialog -> showAddDialog()
            is CategoryIntent.DismissAddDialog -> dismissAddDialog()
            is CategoryIntent.UpdateNewCategoryName -> updateNewCategoryName(intent.name)
            is CategoryIntent.UpdateSelectedColor -> updateSelectedColor(intent.color)
            is CategoryIntent.AddCategory -> addCategory()
            is CategoryIntent.DeleteCategory -> deleteCategory(intent.category)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllCategoriesUseCase().collect { categories ->
                _state.update { it.copy(categories = categories, isLoading = false) }
            }
        }
    }

    private fun showAddDialog() {
        _state.update { it.copy(isAddDialogVisible = true) }
    }

    private fun dismissAddDialog() {
        _state.update {
            it.copy(
                isAddDialogVisible = false,
                newCategoryName = "",
                selectedColor = CategoryColor.BLUE,
            )
        }
    }

    private fun updateNewCategoryName(name: String) {
        _state.update { it.copy(newCategoryName = name) }
    }

    private fun updateSelectedColor(color: CategoryColor) {
        _state.update { it.copy(selectedColor = color) }
    }

    private fun addCategory() {
        val name = _state.value.newCategoryName.trim()
        if (name.isBlank()) {
            viewModelScope.launch {
                _events.send(CategoryEvent.ShowError("カテゴリ名を入力してください"))
            }
            return
        }

        viewModelScope.launch {
            try {
                val category = Category.create(name = name, color = _state.value.selectedColor)
                addCategoryUseCase(category)
                dismissAddDialog()
                _events.send(CategoryEvent.CategoryAdded)
            } catch (e: Exception) {
                _events.send(CategoryEvent.ShowError(e.message ?: "エラーが発生しました"))
            }
        }
    }

    private fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                deleteCategoryUseCase(category.id)
                _events.send(CategoryEvent.CategoryDeleted)
            } catch (e: Exception) {
                _events.send(CategoryEvent.ShowError(e.message ?: "削除に失敗しました"))
            }
        }
    }
}
