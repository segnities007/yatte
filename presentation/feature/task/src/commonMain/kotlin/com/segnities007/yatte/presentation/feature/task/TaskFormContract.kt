package com.segnities007.yatte.presentation.feature.task

import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

/**
 * タスク追加/編集画面の状態
 */
data class TaskFormState(
    val title: String = "",
    val time: LocalTime = LocalTime(9, 0),
    val minutesBefore: Int = 10,
    val taskType: TaskType = TaskType.ONE_TIME,
    val selectedWeekDays: Set<DayOfWeek> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false,
    val editingTaskId: String? = null,
    val soundUri: String? = null,
    val soundName: String? = null,
    val categoryId: CategoryId? = null,
    val categories: List<Category> = emptyList(),
)

/**
 * タスク追加/編集画面のインテント
 */
sealed interface TaskFormIntent {
    data class UpdateTitle(val title: String) : TaskFormIntent
    data class UpdateTime(val time: LocalTime) : TaskFormIntent
    data class UpdateMinutesBefore(val minutes: Int) : TaskFormIntent
    data class UpdateTaskType(val type: TaskType) : TaskFormIntent
    data class ToggleWeekDay(val day: DayOfWeek) : TaskFormIntent
    data class UpdateSoundUri(val uri: String?) : TaskFormIntent
    data class UpdateCategory(val categoryId: CategoryId?) : TaskFormIntent
    data object SaveTask : TaskFormIntent
    data object DeleteTask : TaskFormIntent
    data object Cancel : TaskFormIntent
}

/**
 * タスク追加/編集画面のイベント
 */
sealed interface TaskFormEvent {
    data object TaskSaved : TaskFormEvent
    data object TaskDeleted : TaskFormEvent
    data object Cancelled : TaskFormEvent
    data class ShowError(val message: String) : TaskFormEvent
}

