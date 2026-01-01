package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.designsystem.component.list.YatteTimelineRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * タスクリスト表示
 */
@Composable
fun TaskList(
    tasks: List<Task>,
    onCompleteTask: (Task) -> Unit,
    onSkipTask: (Task) -> Unit,
    onSnoozeTask: (Task) -> Unit,
    onTaskClick: (Task) -> Unit,
    contentPadding: PaddingValues = PaddingValues(YatteSpacing.md),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
    ) {
        itemsIndexed(tasks, key = { _, task -> task.id.value }) { index, task ->
            val isFirst = index == 0
            val isLast = index == tasks.lastIndex

            YatteTimelineRow(
                time = "${task.time.hour.toString().padStart(2, '0')}:${task.time.minute.toString().padStart(2, '0')}",
                lineColor = YatteColors.primary,
                isFirst = isFirst,
                isLast = isLast,
            ) {
                TaskCard(
                    task = task,
                    onComplete = { onCompleteTask(task) },
                    onClick = { onTaskClick(task) },
                    onSnooze = { onSnoozeTask(task) },
                    onSkip = if (task.taskType == TaskType.WEEKLY_LOOP) {
                        { onSkipTask(task) }
                    } else {
                        null
                    },
                )
            }
        }
    }
}
