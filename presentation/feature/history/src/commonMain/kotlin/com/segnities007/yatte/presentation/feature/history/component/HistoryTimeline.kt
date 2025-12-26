package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.history.model.History

/**
 * タイムラインビュー - 時間軸に沿ってタスク完了履歴を表示
 */
@Composable
fun HistoryTimeline(
    items: List<History>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
) {
    val sortedItems = items.sortedByDescending { it.completedAt }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        items(sortedItems, key = { it.id.value }) { history ->
            TimelineItem(
                history = history,
                isFirst = sortedItems.firstOrNull() == history,
                isLast = sortedItems.lastOrNull() == history,
            )
        }
    }
}

@Composable
private fun TimelineItem(
    history: History,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val dotColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        // 時刻表示
        Column(
            modifier = Modifier.width(56.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "${history.completedAt.hour.toString().padStart(2, '0')}:${history.completedAt.minute.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // タイムライン（縦線とドット）
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(72.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxHeight().width(2.dp)) {
                val centerX = size.width / 2
                // 上の線（最初の項目以外）
                if (!isFirst) {
                    drawLine(
                        color = lineColor,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, size.height / 2 - 8.dp.toPx()),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
                // 下の線（最後の項目以外）
                if (!isLast) {
                    drawLine(
                        color = lineColor,
                        start = Offset(centerX, size.height / 2 + 8.dp.toPx()),
                        end = Offset(centerX, size.height),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
            }
            // ドット
            Canvas(modifier = Modifier.width(16.dp).height(16.dp)) {
                drawCircle(
                    color = dotColor,
                    radius = 8.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2),
                )
            }
        }

        // タスク情報カード
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = history.title,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "${history.completedAt.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
