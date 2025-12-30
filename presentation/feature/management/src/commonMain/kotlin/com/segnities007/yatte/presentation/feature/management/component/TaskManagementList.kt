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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.segnities007.yatte.presentation.core.util.toDisplayString
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.management.generated.resources.notification_minutes_before
import yatte.presentation.feature.management.generated.resources.task_type_one_time
import yatte.presentation.feature.management.generated.resources.task_type_weekly
// Weekday resources removed

import yatte.presentation.feature.management.generated.resources.Res as ManagementRes

@Composable
fun TaskManagementList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    contentPadding: PaddingValues = PaddingValues(YatteSpacing.md),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
    ) {
        items(tasks, key = { it.id.value }) { task ->
            TaskManagementCard(
                task = task,
                onClick = { onTaskClick(task) },
            )
        }
    }
}

@Composable
fun TaskManagementCard(
    task: Task,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    YatteCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(YatteSpacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "${task.time.hour}:${task.time.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(YatteSpacing.xxs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val typeLabel = if (task.taskType == TaskType.WEEKLY_LOOP) {
                    val days = buildString {
                        for (day in task.weekDays) {
                            if (isNotEmpty()) append("・")
                            append(day.toDisplayString())
                        }
                    }
                    stringResource(ManagementRes.string.task_type_weekly, days)
                } else {
                    stringResource(ManagementRes.string.task_type_one_time)
                }
                Text(
                    text = typeLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(
                        ManagementRes.string.notification_minutes_before,
                        task.minutesBefore,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}



