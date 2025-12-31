package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.input.YatteChip
import com.segnities007.yatte.presentation.designsystem.component.input.YatteTextField
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.task.TaskFormIntent
import com.segnities007.yatte.presentation.feature.task.TaskFormState
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.category_none
import yatte.presentation.feature.task.generated.resources.field_task_name
import yatte.presentation.feature.task.generated.resources.section_basic_info
import yatte.presentation.feature.task.generated.resources.section_category
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskFormBasicInfoSection(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
) {
    YatteSectionCard(
        title = stringResource(TaskRes.string.section_basic_info),
        icon = Icons.Default.Edit
    ) {
        YatteTextField(
            value = state.title,
            onValueChange = { onIntent(TaskFormIntent.UpdateTitle(it)) },
            label = stringResource(TaskRes.string.field_task_name),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        if (state.categories.isNotEmpty()) {
            YatteText(
                text = stringResource(TaskRes.string.section_category),
                style = YatteTheme.typography.labelMedium,
                color = YatteTheme.colors.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
            ) {
                YatteChip(
                    label = stringResource(TaskRes.string.category_none),
                    selected = state.categoryId == null,
                    onClick = { onIntent(TaskFormIntent.UpdateCategory(null)) },
                )
                state.categories.forEach { category ->
                    YatteChip(
                        label = category.name,
                        selected = state.categoryId == category.id,
                        onClick = { onIntent(TaskFormIntent.UpdateCategory(category.id)) },
                    )
                }
            }
        }
    }
}

@Composable
@Preview
@Preview
fun TaskFormBasicInfoSectionPreview() {
    YatteTheme {
        TaskFormBasicInfoSection(
            state = TaskFormState(),
            onIntent = {},
        )
    }
}
