package com.segnities007.yatte.presentation.feature.home

import com.segnities007.yatte.domain.aggregate.task.model.Task
import kotlinx.datetime.LocalDate

/**
 * ホーム画面の状態
 */
data class HomeState(
    val selectedDate: LocalDate? = null,
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

/**
 * ホーム画面のインテント（ユーザーアクション）
 */
sealed interface HomeIntent {
    data object LoadTasks : HomeIntent
    data class SelectDate(val date: LocalDate) : HomeIntent
    data class CompleteTask(val task: Task) : HomeIntent
    data class SkipTask(val task: Task, val until: LocalDate) : HomeIntent
    data object NavigateToAddTask : HomeIntent
    data object NavigateToHistory : HomeIntent
    data object NavigateToSettings : HomeIntent
    data class NavigateToEditTask(val taskId: String) : HomeIntent
}

/**
 * ホーム画面のイベント（一度だけ発生）
 */
sealed interface HomeEvent {
    data object NavigateToAddTask : HomeEvent
    data object NavigateToHistory : HomeEvent
    data object NavigateToSettings : HomeEvent
    data class NavigateToEditTask(val taskId: String) : HomeEvent
    data class ShowError(val message: String) : HomeEvent
    data class ShowTaskCompleted(val taskTitle: String) : HomeEvent
}

