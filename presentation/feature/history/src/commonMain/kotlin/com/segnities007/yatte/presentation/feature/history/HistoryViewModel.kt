package com.segnities007.yatte.presentation.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.usecase.ClearAllHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.DeleteHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.ExportHistoryUseCase
import com.segnities007.yatte.domain.aggregate.history.usecase.GetHistoryTimelineUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.getString
import yatte.presentation.core.generated.resources.error_delete_failed
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.history.generated.resources.*
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

class HistoryViewModel(
    private val getHistoryTimelineUseCase: GetHistoryTimelineUseCase,
    private val deleteHistoryUseCase: DeleteHistoryUseCase,
    private val clearAllHistoryUseCase: ClearAllHistoryUseCase,
    private val exportHistoryUseCase: ExportHistoryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    private val _events = Channel<HistoryEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var loadHistoryJob: Job? = null

    init {
        loadHistory()
    }

    fun onIntent(intent: HistoryIntent) {
        when (intent) {
            is HistoryIntent.LoadHistory -> loadHistory()
            is HistoryIntent.SelectDate -> selectDate(intent.date)
            is HistoryIntent.DeleteHistory -> deleteHistory(intent.history)
            is HistoryIntent.ClearAllHistory -> clearAllHistory()
            is HistoryIntent.ExportHistory -> exportHistory()
            is HistoryIntent.NavigateBack -> sendEvent(HistoryEvent.NavigateBack)
        }
    }

    private fun loadHistory() {
        loadHistoryJob?.cancel()
        loadHistoryJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getHistoryTimelineUseCase.getAll().collect { items ->
                _state.update { it.copy(historyItems = items, isLoading = false) }
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
    }

    private fun deleteHistory(history: History) {
        viewModelScope.launch {
            val result = deleteHistoryUseCase(history.id)
            result.exceptionOrNull()?.let { error ->
                val message = error.message ?: getString(CoreRes.string.error_delete_failed)
                sendEvent(HistoryEvent.ShowError(message))
            }
        }
    }

    private fun clearAllHistory() {
        viewModelScope.launch {
            clearAllHistoryUseCase()
                .onFailure { error ->
                    val message = error.message ?: getString(HistoryRes.string.error_clear_all_failed)
                    sendEvent(HistoryEvent.ShowError(message))
                }
        }
    }

    private fun exportHistory() {
        viewModelScope.launch {
            exportHistoryUseCase.toJson()
                .onSuccess { _ ->
                    sendEvent(HistoryEvent.ShowExportSuccess("json"))
                }
                .onFailure { error ->
                    val message = error.message ?: getString(HistoryRes.string.error_export_failed)
                    sendEvent(HistoryEvent.ShowError(message))
                }
        }
    }



    private fun sendEvent(event: HistoryEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
