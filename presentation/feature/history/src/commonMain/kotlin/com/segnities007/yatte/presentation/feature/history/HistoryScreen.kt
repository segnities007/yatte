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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.component.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.YatteLoadingIndicator
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.history.model.History
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

    // グローバルHeaderの設定（SideEffectで即座に更新）
    val setHeaderConfig = LocalSetHeaderConfig.current
    val historyTitle = stringResource(HistoryRes.string.title_history)
    
    val headerConfig = remember {
        HeaderConfig(
            title = { Text(historyTitle) },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }

    // YatteScaffold を使用（Headerはグローバルなので省略）
    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
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
                    EmptyHistoryView(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    HistoryTimeline(
                        items = state.historyItems,
                        contentPadding = listContentPadding,
                    )
                }
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

@Composable
private fun HistoryCard(
    history: History,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(YatteSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = history.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(YatteSpacing.xxs))
                Text(
                    text = "${history.completedAt.date} ${history.completedAt.hour}:${history.completedAt.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            YatteIconButton(
                icon = Icons.Default.Delete,
                onClick = onDelete,
                contentDescription = stringResource(CoreRes.string.common_delete),
                tint = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun EmptyHistoryView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(YatteSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 大きなアイコン
        Text(
            text = stringResource(HistoryRes.string.common_empty_emoji),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.5f,
            ),
        )
        Spacer(modifier = Modifier.height(YatteSpacing.lg))
        // タイトル
        Text(
            text = stringResource(HistoryRes.string.empty_no_history),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(YatteSpacing.xs))
        // 説明文
        Text(
            text = stringResource(HistoryRes.string.empty_history_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

