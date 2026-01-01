package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Warning
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryStatus
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.component.display.YatteMetric
import com.segnities007.yatte.presentation.designsystem.component.display.MetricStyle

@Composable
fun HistoryDashboard(
    historyItems: List<History>,
    modifier: Modifier = Modifier,
) {
    val stats = remember(historyItems) {
        HistoryStatistics(
            totalCompleted = historyItems.count { it.status == HistoryStatus.COMPLETED },
            totalSkipped = historyItems.count { it.status == HistoryStatus.SKIPPED },
            totalMissed = historyItems.count { it.status == HistoryStatus.EXPIRED },
        )
    }

    YatteCard(
        modifier = modifier.fillMaxWidth().padding(horizontal = YatteSpacing.md),
        onClick = null
    ) {
        Column(
            modifier = Modifier.padding(YatteSpacing.md)
        ) {
            YatteText(
                text = "TOTAL RESULT",
                style = YatteTheme.typography.labelLarge,
                color = YatteTheme.colors.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = YatteSpacing.sm)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Main Stat: Completed
                YatteMetric(
                    value = stats.totalCompleted.toString(),
                    label = "COMPLETED",
                    icon = Icons.Default.EmojiEvents,
                    color = YatteTheme.colors.primary,
                    style = MetricStyle.Primary,
                    modifier = Modifier.weight(1f)
                )

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(0.8f)
                        .background(YatteTheme.colors.outlineVariant)
                )

                // Sub Stats: Skipped & Missed
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = YatteSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(YatteSpacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    YatteMetric(
                        value = stats.totalSkipped.toString(),
                        label = "SKIPPED",
                        icon = Icons.Default.FastForward,
                        color = YatteTheme.colors.secondary,
                        style = MetricStyle.List
                    )

                    YatteMetric(
                        value = stats.totalMissed.toString(),
                        label = "MISSED",
                        icon = Icons.Default.Warning,
                        color = YatteTheme.colors.error,
                        style = MetricStyle.List
                    )
                }
            }
        }
    }
}

private data class HistoryStatistics(
    val totalCompleted: Int,
    val totalSkipped: Int,
    val totalMissed: Int,
)
