package com.segnities007.yatte.presentation.feature.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.domain.aggregate.task.usecase.CreateTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetTaskByIdUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.UpdateTaskUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * タスク追加/編集のViewModel
 */
class TaskFormViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TaskFormState())
    val state: StateFlow<TaskFormState> = _state.asStateFlow()

    private val _events = Channel<TaskFormEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getTaskByIdUseCase(TaskId(taskId))
                .onSuccess { task ->
                    if (task != null) {
                        _state.update {
                            it.copy(
                                title = task.title,
                                time = task.time,
                                minutesBefore = task.minutesBefore,
                                taskType = task.taskType,
                                selectedWeekDays = task.weekDays.toSet(),
                                isLoading = false,
                                isEditMode = true,
                                editingTaskId = taskId,
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        sendEvent(TaskFormEvent.ShowError("タスクが見つかりません"))
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(TaskFormEvent.ShowError(error.message ?: "タスクの読み込みに失敗しました"))
                }
        }
    }


    fun onIntent(intent: TaskFormIntent) {
        when (intent) {
            is TaskFormIntent.UpdateTitle -> updateTitle(intent.title)
            is TaskFormIntent.UpdateTime -> updateTime(intent.time)
            is TaskFormIntent.UpdateMinutesBefore -> updateMinutesBefore(intent.minutes)
            is TaskFormIntent.UpdateTaskType -> updateTaskType(intent.type)
            is TaskFormIntent.ToggleWeekDay -> toggleWeekDay(intent.day)
            is TaskFormIntent.SaveTask -> saveTask()
            is TaskFormIntent.Cancel -> sendEvent(TaskFormEvent.Cancelled)
        }
    }

    private fun updateTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    private fun updateTime(time: LocalTime) {
        _state.update { it.copy(time = time) }
    }

    private fun updateMinutesBefore(minutes: Int) {
        _state.update { it.copy(minutesBefore = minutes) }
    }

    private fun updateTaskType(type: TaskType) {
        _state.update { it.copy(taskType = type) }
    }

    private fun toggleWeekDay(day: DayOfWeek) {
        _state.update { current ->
            val newDays = if (day in current.selectedWeekDays) {
                current.selectedWeekDays - day
            } else {
                current.selectedWeekDays + day
            }
            current.copy(selectedWeekDays = newDays)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveTask() {
        val currentState = _state.value
        if (currentState.title.isBlank()) {
            sendEvent(TaskFormEvent.ShowError("タイトルを入力してください"))
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val now = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val task = Task(
                id = TaskId(currentState.editingTaskId ?: Uuid.random().toString()),
                title = currentState.title,
                time = currentState.time,
                minutesBefore = currentState.minutesBefore,
                taskType = currentState.taskType,
                weekDays = currentState.selectedWeekDays.toList(),
                isCompleted = false,
                createdAt = now,
                alarmTriggeredAt = null,
                skipUntil = null,
            )

            val result = if (currentState.isEditMode) {
                updateTaskUseCase(task)
            } else {
                createTaskUseCase(task)
            }

            result
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(TaskFormEvent.TaskSaved)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(TaskFormEvent.ShowError(error.message ?: "保存に失敗しました"))
                }
        }
    }

    private fun sendEvent(event: TaskFormEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
