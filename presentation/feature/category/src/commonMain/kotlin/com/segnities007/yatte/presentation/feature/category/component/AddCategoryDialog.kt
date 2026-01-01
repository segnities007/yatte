package com.segnities007.yatte.presentation.feature.category.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
}

@Composable
@Preview
fun AddCategoryDialogPreview() {
    MaterialTheme {
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
