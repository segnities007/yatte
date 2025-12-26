package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.task.model.Task

/**
 * タスクリスト表示
 */
@Composable
fun TaskList(
    tasks: List<Task>,
    onCompleteTask: (Task) -> Unit,
    onSkipTask: (Task) -> Unit,
    onTaskClick: (Task) -> Unit,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(tasks, key = { it.id.value }) { task ->
            TaskCard(
                task = task,
                onComplete = { onCompleteTask(task) },
                onClick = { onTaskClick(task) },
                onSkip = if (task.taskType == com.segnities007.yatte.domain.aggregate.task.model.TaskType.WEEKLY_LOOP) {
                    { onSkipTask(task) }
                } else {
                    null
                },
            )
        }
    }
}
