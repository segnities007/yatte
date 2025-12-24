package com.segnities007.yatte.presentation.feature.home

import com.segnities007.yatte.domain.aggregate.task.model.Task
import kotlinx.datetime.LocalDate

/**
 * ホーム画面の状態
 */
data class HomeState(
    val todayTasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val today: LocalDate? = null,
)

/**
 * ホーム画面のインテント（ユーザーアクション）
 */
sealed interface HomeIntent {
    data object LoadTasks : HomeIntent
    data class CompleteTask(val task: Task) : HomeIntent
    data class SkipTask(val task: Task, val until: LocalDate) : HomeIntent
    data object NavigateToAddTask : HomeIntent
    data object NavigateToHistory : HomeIntent
    data object NavigateToSettings : HomeIntent
}

/**
 * ホーム画面のイベント（一度だけ発生）
 */
sealed interface HomeEvent {
    data object NavigateToAddTask : HomeEvent
    data object NavigateToHistory : HomeEvent
    data object NavigateToSettings : HomeEvent
    data class ShowError(val message: String) : HomeEvent
    data class ShowTaskCompleted(val taskTitle: String) : HomeEvent
}
