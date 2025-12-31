package com.segnities007.yatte.presentation.feature.management.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.segnities007.yatte.presentation.core.util.toDisplayString
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.management.generated.resources.notification_minutes_before
import yatte.presentation.feature.management.generated.resources.task_type_one_time
import yatte.presentation.feature.management.generated.resources.task_type_weekly

import yatte.presentation.feature.management.generated.resources.Res as ManagementRes
import kotlin.time.Clock

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
                YatteText(
                    text = task.title,
                    style = YatteTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                )
                YatteText(
                    text = "${task.time.hour}:${task.time.minute.toString().padStart(2, '0')}",
                    style = YatteTheme.typography.titleMedium,
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
                YatteText(
                    text = typeLabel,
                    style = YatteTheme.typography.bodySmall,
                    color = YatteTheme.colors.onSurfaceVariant,
                )
                YatteText(
                    text = stringResource(
                        ManagementRes.string.notification_minutes_before,
                        task.minutesBefore,
                    ),
                    style = YatteTheme.typography.bodySmall,
                    color = YatteTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
@Preview
fun TaskManagementCardPreview() {
    YatteTheme {
        TaskManagementCard(
            task = Task(
                id = TaskId("1"),
                title = "Weekly Meeting",
                time = LocalTime(14, 0),
                createdAt = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()).toLocalDateTime(TimeZone.currentSystemDefault()),
                taskType = TaskType.WEEKLY_LOOP,
                weekDays = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                minutesBefore = 15,
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
                Task(
                    id = TaskId("1"),
                    title = "Weekly Meeting",
                    time = LocalTime(14, 0),
                    createdAt = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()).toLocalDateTime(TimeZone.currentSystemDefault()),
                )
            ),
            onTaskClick = {},
        )
    }
}
