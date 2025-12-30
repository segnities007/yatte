package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteStickyDateHeader
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import androidx.compose.foundation.layout.padding

/**
 * タイムラインビュー - 時間軸に沿ってタスク完了履歴を表示
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryTimeline(
    items: List<History>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
) {
    val groupedItems = items
        .sortedByDescending { it.completedAt }
        .groupBy { it.completedAt.date }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        item {
            HistoryDashboard(
                historyItems = items,
                modifier = Modifier.padding(bottom = YatteSpacing.md)
            )
        }

        groupedItems.forEach { (date, callbackItems) ->
            stickyHeader(key = date) {
                YatteStickyDateHeader(date = date)
            }

            items(callbackItems, key = { it.id.value }) { history ->
                HistoryTimelineItem(
                    history = history,
                    isFirst = callbackItems.first() == history,
                    isLast = callbackItems.last() == history,
                )
            }
        }
    }
}
