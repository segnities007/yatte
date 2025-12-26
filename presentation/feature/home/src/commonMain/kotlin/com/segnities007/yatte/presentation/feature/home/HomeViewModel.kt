package com.segnities007.yatte.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.usecase.AddHistoryUseCase
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.usecase.CompleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetTodayTasksUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.SkipTaskUseCase
import com.segnities007.yatte.domain.aggregate.alarm.usecase.CancelAlarmUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import yatte.presentation.feature.home.generated.resources.*
import yatte.presentation.feature.home.generated.resources.Res as HomeRes
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HomeViewModel(
    private val getTodayTasksUseCase: GetTodayTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val skipTaskUseCase: SkipTaskUseCase,
    private val addHistoryUseCase: AddHistoryUseCase,
    private val cancelAlarmUseCase: CancelAlarmUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(selectedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()))
    )
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var loadTasksJob: Job? = null

    init {
        loadTasks()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadTasks -> loadTasks()
            is HomeIntent.SelectDate -> selectDate(intent.date)
            is HomeIntent.CompleteTask -> completeTask(intent.task)
            is HomeIntent.SkipTask -> skipTask(intent.task, intent.until)
            is HomeIntent.NavigateToAddTask -> sendEvent(HomeEvent.NavigateToAddTask)
            is HomeIntent.NavigateToHistory -> sendEvent(HomeEvent.NavigateToHistory)
            is HomeIntent.NavigateToSettings -> sendEvent(HomeEvent.NavigateToSettings)
            is HomeIntent.NavigateToEditTask -> sendEvent(HomeEvent.NavigateToEditTask(intent.taskId))
            is HomeIntent.NavigateToToday -> { /* Handled directly in UI via pagerState */ }
        }
    }

    private fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        loadTasks()
    }

    private fun loadTasks() {
        loadTasksJob?.cancel()
        loadTasksJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val selectedDate = _state.value.selectedDate
            getTodayTasksUseCase().collect { allTasks ->
                // 選択された日付に該当するタスクをフィルタ
                val tasksForDate = if (selectedDate != null) {
                    allTasks.filter { it.isActiveOn(selectedDate) }
                } else {
                    allTasks
                }
                _state.update {
                    it.copy(
                        tasks = tasksForDate,
                        isLoading = false,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun completeTask(task: Task) {
        viewModelScope.launch {
            completeTaskUseCase(task.id)
                .onSuccess { completedTask ->
                    // 手動完了時は必ずアラームをキャンセル
                    cancelAlarmUseCase.byTaskId(completedTask.id)

                    // 履歴に追加
                    val now = currentLocalDateTime()
                    val history = History(
                        id = HistoryId(Uuid.random().toString()),
                        taskId = completedTask.id,
                        title = completedTask.title,
                        completedAt = now,
                    )
                    addHistoryUseCase(history)
                    sendEvent(HomeEvent.ShowTaskCompleted(completedTask.title))
                }
                .onFailure { error ->
                    val message = error.message ?: getString(HomeRes.string.error_task_complete_failed)
                    sendEvent(HomeEvent.ShowError(message))
                }
        }
    }

    private fun skipTask(task: Task, until: LocalDate) {
        viewModelScope.launch {
            skipTaskUseCase(task.id, until)
                .onFailure { error ->
                    val message = error.message ?: getString(HomeRes.string.error_skip_failed)
                    sendEvent(HomeEvent.ShowError(message))
                }
        }
    }

    private fun sendEvent(event: HomeEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private fun currentLocalDateTime(): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }
}

