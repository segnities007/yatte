package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.segnities007.yatte.presentation.designsystem.component.card.YatteActionCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun CategoryItem(
    category: Category,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YatteActionCard(
        title = category.name,
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = YatteSpacing.md, vertical = YatteSpacing.xs),
        supportingContent = {
            Box(
                modifier = Modifier
                    .size(YatteSpacing.lg)
                    .clip(CircleShape)
                    .background(Color(category.color.hex)),
            )
        },
        actions = {
            YatteIconButton(
                icon = Icons.Default.Delete,
                onClick = onDelete,
                contentDescription = "削除",
                tint = YatteTheme.colors.error,
            )
        }
    )
}

@Composable
@Preview
private fun CategoryItemPreview() {
    YatteTheme {
        CategoryItem(
            category = Category(
                id = CategoryId("1"),
                name = "Shopping",
                color = CategoryColor.BLUE,
                createdAt = kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0),
            ),
            onDelete = {},
        )
    }
}
