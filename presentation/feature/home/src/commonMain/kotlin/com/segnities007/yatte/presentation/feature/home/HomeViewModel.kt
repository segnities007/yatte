package com.segnities007.yatte.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.alarm.usecase.CancelAlarmUseCase
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.usecase.AddHistoryUseCase
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.usecase.CompleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetAllTasksUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.SkipTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.UncompleteTaskUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.getString
import yatte.presentation.feature.home.generated.resources.error_skip_failed
import yatte.presentation.feature.home.generated.resources.error_task_complete_failed
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

class HomeViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase,
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
            is HomeIntent.CompleteTask -> completeTask(intent.task, intent.date)
            is HomeIntent.UndoCompleteTask -> uncompleteTask(intent.task, intent.date)
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
    }

    private fun loadTasks() {
        loadTasksJob?.cancel()
        loadTasksJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getAllTasksUseCase().collect { allTasks ->
                // 全タスクを保持し、フィルタリングはUI側で行う
                _state.update {
                    it.copy(
                        allTasks = allTasks,
                        isLoading = false,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun completeTask(task: Task, date: LocalDate) {
        viewModelScope.launch {
            completeTaskUseCase(task.id, date)
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
                    sendEvent(HomeEvent.ShowTaskCompleted(task, date))
                }
                .onFailure { error ->
                    val message = error.message ?: getString(HomeRes.string.error_task_complete_failed)
                    sendEvent(HomeEvent.ShowError(message))
                }
        }
    }

    private fun uncompleteTask(task: Task, date: LocalDate) {
        viewModelScope.launch {
            uncompleteTaskUseCase(task.id, date)
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

