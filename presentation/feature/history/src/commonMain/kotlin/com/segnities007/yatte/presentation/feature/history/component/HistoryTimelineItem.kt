package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryStatus
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.component.list.YatteTimelineRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

@Composable
fun HistoryTimelineItem(
    history: History,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    val statusColor = when (history.status) {
        HistoryStatus.COMPLETED -> YatteColors.primary
        HistoryStatus.SKIPPED -> YatteColors.sky
        HistoryStatus.EXPIRED -> YatteColors.coral
    }

    YatteTimelineRow(
        time = "${history.completedAt.hour.toString().padStart(2, '0')}:${history.completedAt.minute.toString().padStart(2, '0')}",
        lineColor = statusColor,
        isFirst = isFirst,
        isLast = isLast,
        modifier = modifier
    ) {
        // タスク情報カード
        YatteCard(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = YatteSpacing.xs),
            onClick = null,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(YatteSpacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 左側: タイトル
                YatteText(
                    text = history.title,
                    style = YatteTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // 右側: ステータスバッジ
                Spacer(modifier = Modifier.width(YatteSpacing.sm))
                HistoryStatusBadge(status = history.status)
            }
        }
    }
}

