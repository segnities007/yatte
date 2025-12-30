package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteEmptyState
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.feature.history.HistoryState
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.history.generated.resources.common_empty_emoji
import yatte.presentation.feature.history.generated.resources.empty_history_description
import yatte.presentation.feature.history.generated.resources.empty_no_history
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

@Composable
fun HistoryContent(
    state: HistoryState,
    contentPadding: PaddingValues,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            state.isLoading -> {
                YatteLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            state.historyItems.isEmpty() -> {
                YatteEmptyState(
                    emoji = stringResource(HistoryRes.string.common_empty_emoji),
                    message = stringResource(HistoryRes.string.empty_no_history),
                    description = stringResource(HistoryRes.string.empty_history_description),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            else -> {
                HistoryTimeline(
                    items = state.historyItems,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}
