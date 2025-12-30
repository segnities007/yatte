package com.segnities007.yatte.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.usecase.CancelAlarmUseCase
import com.segnities007.yatte.domain.aggregate.alarm.usecase.ScheduleAlarmUseCase
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.usecase.AddHistoryUseCase
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.usecase.CompleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.GetAllTasksUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.SkipTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.UncompleteTaskUseCase
import com.segnities007.yatte.domain.aggregate.task.usecase.UpdateTaskUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.getString
import yatte.presentation.feature.home.generated.resources.error_skip_failed
import yatte.presentation.feature.home.generated.resources.error_task_complete_failed
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
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
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val scheduleAlarmUseCase: ScheduleAlarmUseCase,
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
            is HomeIntent.SnoozeTask -> snoozeTask(intent.task)
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

    private fun snoozeTask(task: Task) {
        viewModelScope.launch {
            val nowInstant = Clock.System.now()
            val timeZone = TimeZone.currentSystemDefault()
            val today = nowInstant.toLocalDateTime(timeZone).date
            
            // タスクの現在の予定時刻をInstantに変換（今日の日付と仮定）
            // 日付をまたぐケース（深夜など）は、現在時刻と比較することで補正される
            // 例: タスク時間が 23:55 で 現在が 00:05 (翌日) の場合、
            // taskDateInstant は昨日の23:55になるべきだが、ここでは今日の23:55になる。
            // 比較結果、Now(00:05) < Task(23:55) となり、BaseはTask(今日の23:55)になる。
            // -> 結果、今日の 00:05 から見ると未来すぎる（約24時間後）になる可能性がある。
            // しかし、Snoozeはあくまで「ちょっと先送り」なので、
            // もしタスク時間が未来すぎる（例えば12時間以上先）場合は、「現在時刻」を基準にするガードを入れると安全。
            // ここではシンプルに、「現在時刻と近い過去/未来」を優先するため、
            // TaskTimeが現在より少し未来ならそれを採用、過去なら現在を採用する。
            
            val taskDateTime = LocalDateTime(today, task.time)
            val taskInstant = taskDateTime.toInstant(timeZone)
            
            // タスク時間が現在より未来なら、その時間を基準にする（連打対応）
            // 過去の場合は現在時刻を基準にする（リセット）
            val useTaskTimeAsBase = taskInstant > nowInstant
            val baseInstant = if (useTaskTimeAsBase) taskInstant else nowInstant
            
            // 基準時間から10分後
            val scheduledAtInstant = baseInstant + 10.minutes
            
            // 通知設定
            val notifyAtInstant = scheduledAtInstant - task.minutesBefore.minutes
            
            val newNotifyAt = notifyAtInstant.toLocalDateTime(timeZone)
            val newScheduledAt = scheduledAtInstant.toLocalDateTime(timeZone)
            
            val updatedTask = task.copy(time = newScheduledAt.time)
            
            updateTaskUseCase(updatedTask)
                .onSuccess {
                    // 既存のアラームをキャンセルし、新しい時刻でアラームを設定
                    cancelAlarmUseCase.byTaskId(task.id)
                    
                    val alarm = Alarm(
                        id = AlarmId.generate(),
                        taskId = task.id,
                        scheduledAt = newScheduledAt,
                        notifyAt = newNotifyAt,
                        soundUri = task.soundUri
                    )
                    scheduleAlarmUseCase(alarm)
                }
                .onFailure { error ->
                     val message = error.message ?: "Snooze Failed" 
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
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}

