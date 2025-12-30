package com.segnities007.yatte.presentation.feature.home.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.presentation.designsystem.component.layout.DateHeader
import com.segnities007.yatte.presentation.feature.home.component.TaskCard

import kotlinx.datetime.LocalDateTime

@Preview
@Composable
private fun DateHeaderPreview() {
    MaterialTheme {
        DateHeader(
            selectedDate = LocalDateTime(2024, 1, 1, 12, 0).date,
            onPreviousDay = {},
            onNextDay = {},
            onTodayClick = {}
        )
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    val now = LocalDateTime(2024, 1, 1, 12, 0)
    val mockTask = Task(
        id = TaskId("1"),
        title = "Sample Task",
        time = now.time,
        createdAt = now,
    )
    MaterialTheme {
        TaskCard(
            task = mockTask,
            onComplete = {},
            onClick = {},
            onSkip = {}
        )
    }
}
