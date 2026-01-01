package com.segnities007.yatte.presentation.feature.management.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.card.YatteActionCard
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.feature.management.TaskManagementUiModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskManagementList(
    tasks: List<TaskManagementUiModel>,
    onTaskClick: (taskId: String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(YatteSpacing.md),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
    ) {
        items(tasks, key = { it.id }) { uiModel ->
            TaskManagementCard(
                uiModel = uiModel,
                onClick = { onTaskClick(uiModel.id) },
            )
        }
    }
}

@Composable
fun TaskManagementCard(
    uiModel: TaskManagementUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YatteActionCard(
        title = uiModel.title,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        supportingContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                YatteText(
                    text = uiModel.typeLabel,
                    style = YatteTheme.typography.bodySmall,
                    color = YatteTheme.colors.onSurfaceVariant,
                )
                YatteText(
                    text = uiModel.notificationLabel,
                    style = YatteTheme.typography.bodySmall,
                    color = YatteTheme.colors.onSurfaceVariant,
                )
            }
        },
        actions = {
            YatteText(
                text = uiModel.timeLabel,
                style = YatteTheme.typography.titleMedium,
            )
        }
    )
}

@Composable
@Preview
fun TaskManagementCardPreview() {
    YatteTheme {
        TaskManagementCard(
            uiModel = TaskManagementUiModel(
                id = "1",
                title = "Weekly Meeting",
                timeLabel = "14:00",
                typeLabel = "毎週月・水曜日",
                notificationLabel = "15分前",
            ),
            onClick = {},
        )
    }
}

@Composable
@Preview
fun TaskManagementListPreview() {
    YatteTheme {
        TaskManagementList(
            tasks = listOf(
                TaskManagementUiModel(
                    id = "1",
                    title = "Weekly Meeting",
                    timeLabel = "14:00",
                    typeLabel = "毎週月・水曜日",
                    notificationLabel = "15分前",
                )
            ),
            onTaskClick = {},
        )
    }
}
