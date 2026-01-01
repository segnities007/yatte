package com.segnities007.yatte.presentation.feature.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.alarm.usecase.CancelAlarmUseCase
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.domain.aggregate.task.usecase.DeleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetAllTasksUseCase
import com.segnities007.yatte.presentation.core.formatter.getDisplayString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import yatte.presentation.core.generated.resources.error_delete_failed
import yatte.presentation.feature.management.generated.resources.notification_minutes_before
import yatte.presentation.feature.management.generated.resources.snackbar_task_deleted
import yatte.presentation.feature.management.generated.resources.task_type_one_time
import yatte.presentation.feature.management.generated.resources.task_type_weekly
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.management.generated.resources.Res as ManagementRes

class TaskManagementViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TaskManagementState())
    val state: StateFlow<TaskManagementState> = _state.asStateFlow()

    private val _events = Channel<TaskManagementEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadTasks()
    }

    fun onIntent(intent: TaskManagementIntent) {
        when (intent) {
            is TaskManagementIntent.LoadTasks -> loadTasks()
            is TaskManagementIntent.NavigateToEditTask -> sendEvent(TaskManagementEvent.NavigateToEditTask(intent.taskId))
            is TaskManagementIntent.NavigateToAddTask -> sendEvent(TaskManagementEvent.NavigateToAddTask)
            is TaskManagementIntent.DeleteTask -> deleteTask(intent.taskId)
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllTasksUseCase().collect { tasks ->
                val uiModels = tasks.sortedBy { it.time }.map { task ->
                    val typeLabel = if (task.taskType == TaskType.WEEKLY_LOOP) {
                        val dayStrings = task.weekDays.map { it.getDisplayString() }
                        val days = dayStrings.joinToString("・")
                        getString(ManagementRes.string.task_type_weekly, days)
                    } else {
                        getString(ManagementRes.string.task_type_one_time)
                    }
                    val notificationLabel = getString(
                        ManagementRes.string.notification_minutes_before,
                        task.minutesBefore,
                    )
                    val timeLabel = "${task.time.hour}:${task.time.minute.toString().padStart(2, '0')}"

                    TaskManagementUiModel(
                        id = task.id.value,
                        title = task.title,
                        timeLabel = timeLabel,
                        typeLabel = typeLabel,
                        notificationLabel = notificationLabel,
                    )
                }
                _state.update {
                    it.copy(
                        tasks = uiModels,
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            cancelAlarmUseCase.byTaskId(TaskId(taskId))
            val result = deleteTaskUseCase(TaskId(taskId))
            result
                .onSuccess {
                    sendEvent(TaskManagementEvent.ShowMessage(getString(ManagementRes.string.snackbar_task_deleted)))
                    // Flowを監視しているので自動更新されるはずだが、念の為
                }

            result.exceptionOrNull()?.let { error ->
                val message = error.message ?: getString(CoreRes.string.error_delete_failed)
                sendEvent(TaskManagementEvent.ShowError(message))
            }
        }
    }

    private fun sendEvent(event: TaskManagementEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
