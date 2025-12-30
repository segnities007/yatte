package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.category.CategoryState

@Composable
fun CategoryContent(
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
