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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.segnities007.yatte.presentation.designsystem.component.YatteCard
import com.segnities007.yatte.presentation.designsystem.component.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.component.YatteTextField
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.koin.compose.viewmodel.koinViewModel

// No change needed for CategoryScreen regarding YatteScaffold as it uses M3 Scaffold.
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

    Scaffold(
        topBar = { CategoryTopBar(onBack = onBack) },
        floatingActionButton = {
            CategoryFab(
                onClick = { viewModel.onIntent(CategoryIntent.ShowAddDialog) }
            )
        },
    ) { padding ->
        CategoryContent(
            state = state,
            onDelete = { viewModel.onIntent(CategoryIntent.DeleteCategory(it)) },
            contentPadding = padding,
        )

        CategoryDialogs(
            state = state,
            onNameChange = { viewModel.onIntent(CategoryIntent.UpdateNewCategoryName(it)) },
            onColorSelect = { viewModel.onIntent(CategoryIntent.UpdateSelectedColor(it)) },
            onDismiss = { viewModel.onIntent(CategoryIntent.DismissAddDialog) },
            onConfirm = { viewModel.onIntent(CategoryIntent.AddCategory) },
        )
    }
}

@Composable
private fun CategorySetupSideEffects(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("カテゴリ管理") },
        navigationIcon = {
            YatteIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBack,
                contentDescription = "戻る",
            )
        },
    )
}

@Composable
private fun CategoryFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.bounceClick(),
    ) {
        Icon(Icons.Default.Add, contentDescription = "カテゴリを追加")
    }
}

@Composable
private fun CategoryContent(
    state: CategoryState,
    onDelete: (Category) -> Unit,
    contentPadding: PaddingValues,
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            YatteLoadingIndicator()
        }
    } else if (state.categories.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "カテゴリがありません\n右下の＋ボタンで追加できます",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(YatteSpacing.md),
            verticalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
        ) {
            items(state.categories, key = { it.id.value }) { category ->
                CategoryItem(
                    category = category,
                    onDelete = { onDelete(category) },
                )
            }
        }
    }
}

@Composable
private fun CategoryDialogs(
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

@Composable
private fun CategoryItem(
    category: Category,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YatteCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = YatteSpacing.md, vertical = YatteSpacing.xs),
    ) {
        Column(
            modifier = Modifier.padding(YatteSpacing.md),
            verticalArrangement = Arrangement.spacedBy(YatteSpacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(YatteSpacing.lg)
                        .clip(CircleShape)
                        .background(Color(category.color.hex)),
                )
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            YatteIconButton(
                icon = Icons.Default.Delete,
                onClick = onDelete,
                contentDescription = "削除",
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun AddCategoryDialog(
    name: String,
    selectedColor: CategoryColor,
    onNameChange: (String) -> Unit,
    onColorSelect: (CategoryColor) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("カテゴリを追加") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(YatteSpacing.md)) {
                YatteTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = "カテゴリ名",
                    singleLine = true,
                )
                Text("カラー", style = MaterialTheme.typography.labelMedium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                ) {
                    items(CategoryColor.entries) { color ->
                        ColorOption(
                            color = color,
                            isSelected = color == selectedColor,
                            onClick = { onColorSelect(color) },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.bounceClick(),
            ) {
                Text("追加")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.bounceClick(),
            ) {
                Text("キャンセル")
            }
        },
    )
}

@Composable
private fun ColorOption(
    color: CategoryColor,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(color.hex))
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                } else {
                    Modifier
                },
            )
            .bounceClick(onTap = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "選択済み",
                tint = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
