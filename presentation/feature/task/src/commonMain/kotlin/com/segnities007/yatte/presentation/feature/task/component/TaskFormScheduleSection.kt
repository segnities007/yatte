package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.background

import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.segnities007.yatte.presentation.core.util.toDisplayString
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.input.YatteChip
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.task.TaskFormIntent
import com.segnities007.yatte.presentation.feature.task.TaskFormState
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.label_execute_time
import yatte.presentation.feature.task.generated.resources.label_repeat_weekdays
import yatte.presentation.feature.task.generated.resources.label_task_type
import yatte.presentation.feature.task.generated.resources.section_schedule
import yatte.presentation.feature.task.generated.resources.tap_to_change_time
import yatte.presentation.feature.task.generated.resources.task_type_one_time
import yatte.presentation.feature.task.generated.resources.task_type_weekly
// Weekday resources removed as they are now used from core

import yatte.presentation.feature.task.generated.resources.Res as TaskRes

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskFormScheduleSection(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
) {
    YatteSectionCard(
        title = stringResource(TaskRes.string.section_schedule),
        icon = Icons.Default.Schedule,
    ) {
        Text(
             text = stringResource(TaskRes.string.label_execute_time),
             style = MaterialTheme.typography.labelMedium,
             color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(YatteSpacing.xs))

        var showTimeSheet by remember { mutableStateOf(false) }

        // Schedule Button with 3D Bevel
        val scheduleBrush = YatteBrushes.Yellow.Action
        val scheduleContentColor = MaterialTheme.colorScheme.onSecondary // Dark Brown

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(YatteSpacing.md))
                .bounceClick(onTap = { showTimeSheet = true })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(scheduleBrush, RoundedCornerShape(YatteSpacing.md))
                    .padding(vertical = YatteSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${state.time.hour.toString().padStart(2, '0')}:${
                        state.time.minute.toString().padStart(2, '0')
                    }",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = scheduleContentColor
                )
                Text(
                    text = stringResource(TaskRes.string.tap_to_change_time),
                    style = MaterialTheme.typography.labelSmall,
                    color = scheduleContentColor.copy(alpha = 0.7f)
                )
            }
        }

        if (showTimeSheet) {
            TaskTimePickerSheet(
                initialTime = state.time,
                onDismiss = { showTimeSheet = false },
                onConfirm = { newTime ->
                    onIntent(TaskFormIntent.UpdateTime(newTime))
                    showTimeSheet = false
                }
            )
        }

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        // Weekdays Selection
        val repeatLabel = if (state.taskType == TaskType.ONE_TIME) {
            "${stringResource(TaskRes.string.label_repeat_weekdays)} (${stringResource(TaskRes.string.task_type_one_time)})"
        } else {
             stringResource(TaskRes.string.label_repeat_weekdays)
        }

        Text(
            text = repeatLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(YatteSpacing.xs))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
        ) {
            DayOfWeek.entries.forEach { day ->
                YatteChip(
                    label = day.toDisplayString(),
                    selected = day in state.selectedWeekDays,
                    onClick = { onIntent(TaskFormIntent.ToggleWeekDay(day)) },
                )
            }
        }
    }
}

@Composable
@Preview
fun TaskFormScheduleSectionPreview() {
    MaterialTheme {
        TaskFormScheduleSection(
            state = TaskFormState(),
            onIntent = {},
        )
    }
}


