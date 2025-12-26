package com.segnities007.yatte.presentation.feature.management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.management.generated.resources.*
import yatte.presentation.feature.management.generated.resources.Res as ManagementRes

@Composable
fun TaskManagementList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
            Spacer(modifier = Modifier.height(4.dp))
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

@Composable
private fun DayOfWeek.toDisplayString(): String = when (this) {
    DayOfWeek.MONDAY -> stringResource(ManagementRes.string.weekday_mon_short)
    DayOfWeek.TUESDAY -> stringResource(ManagementRes.string.weekday_tue_short)
    DayOfWeek.WEDNESDAY -> stringResource(ManagementRes.string.weekday_wed_short)
    DayOfWeek.THURSDAY -> stringResource(ManagementRes.string.weekday_thu_short)
    DayOfWeek.FRIDAY -> stringResource(ManagementRes.string.weekday_fri_short)
    DayOfWeek.SATURDAY -> stringResource(ManagementRes.string.weekday_sat_short)
    DayOfWeek.SUNDAY -> stringResource(ManagementRes.string.weekday_sun_short)
}
