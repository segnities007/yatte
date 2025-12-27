package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryStatus
import com.segnities007.yatte.presentation.designsystem.component.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.history.generated.resources.*
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

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
        modifier = modifier.fillMaxSize(), // Ensure it fills the screen to capture nested scroll events properly
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
    val statusColor = when (history.status) {
        HistoryStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        HistoryStatus.SKIPPED -> MaterialTheme.colorScheme.tertiary
        HistoryStatus.EXPIRED -> MaterialTheme.colorScheme.error
    }

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
                        color = statusColor,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, size.height / 2 - 8.dp.toPx()),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
                // 下の線（最後の項目以外）
                if (!isLast) {
                    drawLine(
                        color = statusColor,
                        start = Offset(centerX, size.height / 2 + 8.dp.toPx()),
                        end = Offset(centerX, size.height),
                        strokeWidth = 2.dp.toPx(),
                    )
                }
            }
            // ドット
            Canvas(modifier = Modifier.width(16.dp).height(16.dp)) {
                drawCircle(
                    color = statusColor,
                    radius = 8.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2),
                )
            }
        }

        // タスク情報カード
        YatteCard(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = YatteSpacing.xs),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(YatteSpacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 左側: タイトルと日付
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = history.title,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${history.completedAt.date.month.number}/${history.completedAt.date.day}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                // 右側: ステータスバッジ
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = history.status)
            }
        }
    }
}

@Composable
private fun StatusBadge(
    status: HistoryStatus,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, text) = when (status) {
        HistoryStatus.COMPLETED -> Pair(
            MaterialTheme.colorScheme.primaryContainer,
            stringResource(HistoryRes.string.status_completed),
        )
        HistoryStatus.SKIPPED -> Pair(
            MaterialTheme.colorScheme.tertiaryContainer,
            stringResource(HistoryRes.string.status_skipped),
        )
        HistoryStatus.EXPIRED -> Pair(
            MaterialTheme.colorScheme.errorContainer,
            stringResource(HistoryRes.string.status_expired),
        )
    }

    val textColor = when (status) {
        HistoryStatus.COMPLETED -> MaterialTheme.colorScheme.onPrimaryContainer
        HistoryStatus.SKIPPED -> MaterialTheme.colorScheme.onTertiaryContainer
        HistoryStatus.EXPIRED -> MaterialTheme.colorScheme.onErrorContainer
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
        )
    }
}
