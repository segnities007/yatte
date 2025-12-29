package com.segnities007.yatte.presentation.feature.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import com.segnities007.yatte.domain.aggregate.category.usecase.GetAllCategoriesUseCase
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.usecase.CancelAlarmUseCase
import com.segnities007.yatte.domain.aggregate.alarm.usecase.ScheduleAlarmUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.CreateTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.DeleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetTaskByIdUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.UpdateTaskUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlin.time.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import org.jetbrains.compose.resources.getString
import yatte.presentation.core.generated.resources.*
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.task.generated.resources.*
import yatte.presentation.feature.task.generated.resources.Res as TaskRes
import kotlin.uuid.Uuid
import com.segnities007.yatte.presentation.core.sound.RingtoneUtils

class TaskFormViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
) : ViewModel() {

    init {
        loadCategories()
    }

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
                                soundUri = task.soundUri,
                                soundName = task.soundUri?.let { uri -> RingtoneUtils.getRingtoneTitle(uri) },
                                categoryId = task.categoryId,
                            )
                        }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        sendEvent(TaskFormEvent.ShowError(getString(TaskRes.string.error_task_not_found)))
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    val message = error.message ?: getString(TaskRes.string.error_task_load_failed)
                    sendEvent(TaskFormEvent.ShowError(message))
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
            is TaskFormIntent.UpdateSoundUri -> updateSoundUri(intent.uri)
            is TaskFormIntent.UpdateCategory -> updateCategory(intent.categoryId)
            is TaskFormIntent.SaveTask -> saveTask()
            is TaskFormIntent.DeleteTask -> deleteTask()
            is TaskFormIntent.Cancel -> sendEvent(TaskFormEvent.Cancelled)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase().collect { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }
    }

    private fun updateCategory(categoryId: CategoryId?) {
        _state.update { it.copy(categoryId = categoryId) }
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
            viewModelScope.launch {
                val message = getString(TaskRes.string.error_title_required)
                _events.send(TaskFormEvent.ShowError(message))
            }
            return
        }

        // 週次タスクの場合、曜日が1つ以上選択されていることを確認
        if (currentState.taskType == TaskType.WEEKLY_LOOP && currentState.selectedWeekDays.isEmpty()) {
            viewModelScope.launch {
                val message = getString(TaskRes.string.error_weekdays_required)
                _events.send(TaskFormEvent.ShowError(message))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val now = currentLocalDateTime()

            val task = Task(
                id = TaskId(currentState.editingTaskId ?: Uuid.random().toString()),
                title = currentState.title,
                categoryId = currentState.categoryId,
                time = currentState.time,
                minutesBefore = currentState.minutesBefore,
                taskType = currentState.taskType,
                weekDays = currentState.selectedWeekDays.toList(),
                completedDates = emptySet(),
                createdAt = now,
                alarmTriggeredAt = null,
                skipUntil = null,
                soundUri = currentState.soundUri,
            )

            // 編集時は既存アラームをキャンセルしてから再スケジュール
            cancelAlarmUseCase.byTaskId(task.id)

            val result = if (currentState.isEditMode) {
                updateTaskUseCase(task)
            } else {
                createTaskUseCase(task)
            }

            result
                .onSuccess {
                    // タスク保存に成功したらアラームをスケジュール
                    val alarm = buildAlarm(task, now)
                    if (alarm != null) {
                        scheduleAlarmUseCase(alarm)
                    }
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(TaskFormEvent.TaskSaved)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    val message = error.message ?: getString(TaskRes.string.error_save_failed)
                    sendEvent(TaskFormEvent.ShowError(message))
                }
        }
    }

    private fun deleteTask() {
        val currentState = _state.value
        val taskId = currentState.editingTaskId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            cancelAlarmUseCase.byTaskId(TaskId(taskId))
            val result = deleteTaskUseCase(TaskId(taskId))
            result
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(TaskFormEvent.TaskDeleted)
                }

            result.exceptionOrNull()?.let { error ->
                _state.update { it.copy(isLoading = false) }
                val message = error.message ?: getString(CoreRes.string.error_delete_failed)
                sendEvent(TaskFormEvent.ShowError(message))
            }
        }
    }

    private fun sendEvent(event: TaskFormEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private fun currentLocalDateTime(): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    private fun buildAlarm(task: Task, now: LocalDateTime): Alarm? {
        val scheduledAt = when (task.taskType) {
            TaskType.ONE_TIME -> {
                val candidate = LocalDateTime(task.createdAt.date, task.time)
                // 現在時刻より前なら翌日に設定
                if (candidate <= now) {
                    val nextDay = candidate.date.plus(DatePeriod(days = 1))
                    LocalDateTime(nextDay, task.time)
                } else {
                    candidate
                }
            }
            TaskType.WEEKLY_LOOP -> nextOccurrence(now, task.time, task.weekDays)
        }

        val notifyAt = subtractMinutes(scheduledAt, task.minutesBefore)

        // 通知時間がそれでも過去になる場合は、さらに調整が必要かもしれないが、
        // いったんスケジュール時刻基準で翌日に回すロジックで対応する。
        // もし notifyAt < now になるなら、それは「今すぐ通知」すべきか「諦める」べきかだが、
        // AndroidAlarmSchedulerは過去の時間を渡されると即時発火する可能性がある。
        
        // 念のため notifyAt が過去すぎる（例えば1分以上前）場合はアラームを作らない手もあるが、
        // ユーザーが「今すぐ確認したい」ケースもあるので、そのまま渡す。
        
        return Alarm(
            id = AlarmId.generate(),
            taskId = task.id,
            scheduledAt = scheduledAt,
            notifyAt = notifyAt,
            soundUri = task.soundUri,
        )
    }

    private fun updateSoundUri(uri: String?) {
        val name = uri?.let { RingtoneUtils.getRingtoneTitle(it) }
        _state.update { it.copy(soundUri = uri, soundName = name) }
    }

    private fun subtractMinutes(dateTime: LocalDateTime, minutesBefore: Int): LocalDateTime {
        val tz = TimeZone.currentSystemDefault()
        val instant = dateTime.toInstant(tz)
        return (instant - minutesBefore.minutes).toLocalDateTime(tz)
    }

    private fun nextOccurrence(now: LocalDateTime, time: LocalTime, weekDays: List<DayOfWeek>): LocalDateTime {
        val today = now.date
        for (i in 0..6) {
            val date = today.plus(DatePeriod(days = i))
            if (!weekDays.contains(date.dayOfWeek)) continue

            val candidate = LocalDateTime(date, time)
            if (candidate >= now) return candidate
        }

        val fallback = today.plus(DatePeriod(days = 7))
        return LocalDateTime(fallback, time)
    }
}
