package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButtonStyle
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteDialog
import com.segnities007.yatte.presentation.designsystem.component.input.YatteTextField
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

@Composable
fun AddCategoryDialog(
    name: String,
    selectedColor: CategoryColor,
    onNameChange: (String) -> Unit,
    onColorSelect: (CategoryColor) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    YatteDialog(
        title = "カテゴリを追加",
        onDismiss = onDismiss,
        confirmButton = {
            YatteButton(
                onClick = onConfirm,
                text = "追加",
                style = YatteButtonStyle.Emphasis,
            )
        },
        dismissButton = {
            YatteButton(
                onClick = onDismiss,
                text = "キャンセル",
                style = YatteButtonStyle.Secondary,
            )
        },
    ) {
        YatteTextField(
            value = name,
            onValueChange = onNameChange,
            label = "カテゴリ名",
            singleLine = true,
        )
        YatteText(
            text = "カラー",
            style = YatteTheme.typography.labelMedium,
            color = YatteTheme.colors.onSurfaceVariant
        )
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
}

@Composable
@Preview
fun AddCategoryDialogPreview() {
    YatteTheme {
        AddCategoryDialog(
            name = "New Category",
            selectedColor = CategoryColor.BLUE,
            onNameChange = {},
            onColorSelect = {},
            onDismiss = {},
            onConfirm = {},
        )
    }
}
