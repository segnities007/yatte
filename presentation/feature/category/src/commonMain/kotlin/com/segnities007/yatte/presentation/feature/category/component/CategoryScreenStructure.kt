package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.presentation.designsystem.component.button.YatteFloatingActionButton
import com.segnities007.yatte.presentation.designsystem.component.navigation.YatteTopAppBar
import com.segnities007.yatte.presentation.feature.category.CategoryEvent
import com.segnities007.yatte.presentation.feature.category.CategoryState
import com.segnities007.yatte.presentation.feature.category.CategoryViewModel

@Composable
fun CategorySetupSideEffects(
    viewModel: CategoryViewModel,
    onShowSnackbar: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is CategoryEvent.ShowError -> onShowSnackbar(event.message)
                is CategoryEvent.CategoryAdded -> onShowSnackbar("カテゴリを追加しました")
                is CategoryEvent.CategoryDeleted -> onShowSnackbar("カテゴリを削除しました")
            }
        }
    }
}

@Composable
fun CategoryTopBar(onBack: () -> Unit) {
    YatteTopAppBar(
        title = "カテゴリ管理",
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        navigationContentDescription = "戻る",
        onNavigationClick = onBack,
    )
}

@Composable
fun CategoryFab(onClick: () -> Unit) {
    YatteFloatingActionButton(
        isVisible = true,
        onClick = onClick,
        contentDescription = "カテゴリを追加",
    )
}

@Composable
fun CategoryDialogs(
    state: CategoryState,
    onNameChange: (String) -> Unit,
    onColorSelect: (CategoryColor) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (state.isAddDialogVisible) {
        AddCategoryDialog(
            name = state.newCategoryName,
            selectedColor = state.selectedColor,
            onNameChange = onNameChange,
            onColorSelect = onColorSelect,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
        )
    }
}
