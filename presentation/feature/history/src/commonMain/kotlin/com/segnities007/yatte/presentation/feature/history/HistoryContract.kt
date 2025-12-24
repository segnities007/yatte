package com.segnities007.yatte.presentation.feature.history

import com.segnities007.yatte.domain.aggregate.history.model.History
import kotlinx.datetime.LocalDate

/**
 * 履歴画面の状態
 */
data class HistoryState(
    val historyItems: List<History> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
)

/**
 * 履歴画面のインテント
 */
sealed interface HistoryIntent {
    data object LoadHistory : HistoryIntent
    data class SelectDate(val date: LocalDate) : HistoryIntent
    data class DeleteHistory(val history: History) : HistoryIntent
    data object ClearAllHistory : HistoryIntent
    data object ExportHistory : HistoryIntent
    data object NavigateBack : HistoryIntent
}

/**
 * 履歴画面のイベント
 */
sealed interface HistoryEvent {
    data object NavigateBack : HistoryEvent
    data class ShowError(val message: String) : HistoryEvent
    data class ShowExportSuccess(val format: String) : HistoryEvent
    data object ShowClearConfirmation : HistoryEvent
}
