package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.segnities007.yatte.presentation.feature.history.HistoryActions
import com.segnities007.yatte.presentation.feature.history.HistoryEvent
import com.segnities007.yatte.presentation.feature.history.HistoryViewModel
import org.jetbrains.compose.resources.getString
import yatte.presentation.feature.history.generated.resources.snackbar_export_success
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

@Composable
fun HistorySideEffects(
    viewModel: HistoryViewModel,
    actions: HistoryActions,
    onShowSnackbar: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HistoryEvent.NavigateBack -> actions.onBack()
                is HistoryEvent.ShowError -> onShowSnackbar(event.message)
                is HistoryEvent.ShowExportSuccess -> onShowSnackbar(
                    getString(HistoryRes.string.snackbar_export_success, event.format),
                )
                is HistoryEvent.ShowClearConfirmation -> {}
            }
        }
    }
}
