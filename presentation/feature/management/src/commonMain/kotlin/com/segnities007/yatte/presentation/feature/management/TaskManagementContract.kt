package com.segnities007.yatte.presentation.feature.management

import com.segnities007.yatte.domain.aggregate.task.model.Task

/**
 * タスク管理画面の状態
 */
data class TaskManagementState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * タスク管理画面のインテント
 */
sealed interface TaskManagementIntent {
    data object LoadTasks : TaskManagementIntent
    data class NavigateToEditTask(val taskId: String) : TaskManagementIntent
    data object NavigateToAddTask : TaskManagementIntent
    data class DeleteTask(val taskId: String) : TaskManagementIntent
}

/**
 * タスク管理画面のイベント
 */
sealed interface TaskManagementEvent {
    data class NavigateToEditTask(val taskId: String) : TaskManagementEvent
    data object NavigateToAddTask : TaskManagementEvent
    data class ShowError(val message: String) : TaskManagementEvent
    data class ShowMessage(val message: String) : TaskManagementEvent
}
