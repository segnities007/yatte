package com.segnities007.yatte.presentation.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.YatteScaffold
import com.segnities007.yatte.presentation.core.util.DateFormatter
import com.segnities007.yatte.presentation.designsystem.component.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.component.YatteEmptyState
import com.segnities007.yatte.presentation.feature.history.component.HistoryCard
import com.segnities007.yatte.presentation.feature.history.component.HistoryTimeline
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.common_delete
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.history.generated.resources.*
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel(),
    actions: HistoryActions,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isNavigationVisible: Boolean,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistorySetupHeader()
    HistorySetupSideEffects(
        viewModel = viewModel,
        actions = actions,
        onShowSnackbar = onShowSnackbar
    )

    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        HistoryContent(
            state = state,
            contentPadding = listContentPadding,
        )
    }
}

@Composable
private fun HistorySetupHeader() {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val historyTitle = stringResource(HistoryRes.string.title_history)
    
    val headerConfig = remember {
        HeaderConfig(title = { Text(historyTitle) })
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}

@Composable
private fun HistorySetupSideEffects(
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

@Composable
private fun HistoryContent(
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

@Composable
private fun HistoryList(
    items: List<History>,
    onDelete: (History) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(YatteSpacing.md),
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
    ) {
        items(items, key = { it.id.value }) { history ->
            HistoryCard(
                history = history,
                onDelete = { onDelete(history) },
            )
        }
    }
}


