package com.segnities007.yatte.presentation.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteEmptyState
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.history.component.HistoryCard
import com.segnities007.yatte.presentation.feature.history.component.HistoryTimeline
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.feature.history.generated.resources.common_empty_emoji
import yatte.presentation.feature.history.generated.resources.empty_history_description
import yatte.presentation.feature.history.generated.resources.empty_no_history
import yatte.presentation.feature.history.generated.resources.snackbar_export_success
import yatte.presentation.feature.history.generated.resources.title_history
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

import com.segnities007.yatte.presentation.feature.history.component.HistoryContent
import com.segnities007.yatte.presentation.feature.history.component.HistoryHeader
import com.segnities007.yatte.presentation.feature.history.component.HistorySideEffects

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

    HistoryHeader()
    HistorySideEffects(
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


