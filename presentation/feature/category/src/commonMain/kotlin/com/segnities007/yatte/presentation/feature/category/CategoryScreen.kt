package com.segnities007.yatte.presentation.feature.category

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.component.button.YatteFloatingActionButton
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteDialog
import com.segnities007.yatte.presentation.designsystem.component.input.YatteTextField
import com.segnities007.yatte.presentation.designsystem.component.navigation.YatteTopAppBar
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteBasicScaffold
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.koin.compose.viewmodel.koinViewModel

import com.segnities007.yatte.presentation.feature.category.component.CategoryContent
import com.segnities007.yatte.presentation.feature.category.component.CategoryDialogs
import com.segnities007.yatte.presentation.feature.category.component.CategoryFab
import com.segnities007.yatte.presentation.feature.category.component.CategorySetupSideEffects
import com.segnities007.yatte.presentation.feature.category.component.CategoryTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = koinViewModel(),
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CategorySetupSideEffects(
        viewModel = viewModel,
        onShowSnackbar = onShowSnackbar
    )

    CategoryScreen(
        state = state,
        onBack = onBack,
        onFabClick = { viewModel.onIntent(CategoryIntent.ShowAddDialog) },
        onDeleteCategory = { viewModel.onIntent(CategoryIntent.DeleteCategory(it)) },
        onNameChange = { viewModel.onIntent(CategoryIntent.UpdateNewCategoryName(it)) },
        onColorSelect = { viewModel.onIntent(CategoryIntent.UpdateSelectedColor(it)) },
        onDismissDialog = { viewModel.onIntent(CategoryIntent.DismissAddDialog) },
        onConfirmAdd = { viewModel.onIntent(CategoryIntent.AddCategory) },
    )
}

@Composable
fun CategoryScreen(
    state: CategoryState,
    onBack: () -> Unit,
    onFabClick: () -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onNameChange: (String) -> Unit,
    onColorSelect: (CategoryColor) -> Unit,
    onDismissDialog: () -> Unit,
    onConfirmAdd: () -> Unit,
) {
    YatteBasicScaffold(
        topBar = { CategoryTopBar(onBack = onBack) },
        floatingActionButton = {
            CategoryFab(onClick = onFabClick)
        },
    ) { padding ->
        CategoryContent(
            state = state,
            onDelete = onDeleteCategory,
            contentPadding = padding,
        )

        CategoryDialogs(
            state = state,
            onNameChange = onNameChange,
            onColorSelect = onColorSelect,
            onDismiss = onDismissDialog,
            onConfirm = onConfirmAdd,
        )
    }
}

@Composable
@Preview
fun CategoryScreenPreview() {
    MaterialTheme {
        CategoryScreen(
            state = CategoryState(),
            onBack = {},
            onFabClick = {},
            onDeleteCategory = {},
            onNameChange = {},
            onColorSelect = {},
            onDismissDialog = {},
            onConfirmAdd = {},
        )
    }
}
