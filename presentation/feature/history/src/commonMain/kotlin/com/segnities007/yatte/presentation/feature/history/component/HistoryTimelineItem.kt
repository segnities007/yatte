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
import com.segnities007.yatte.presentation.designsystem.component.list.YatteTimelineCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

@Composable
fun HistoryTimelineItem(
    history: History,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    val tint = when (history.status) {
        HistoryStatus.COMPLETED -> YatteTheme.colors.primary
        HistoryStatus.SKIPPED -> YatteTheme.colors.secondary
        HistoryStatus.EXPIRED -> YatteTheme.colors.error
    }

    YatteTimelineCard(
        time = "${history.completedAt.hour.toString().padStart(2, '0')}:${history.completedAt.minute.toString().padStart(2, '0')}",
        modifier = modifier.fillMaxWidth(),
        isFirst = isFirst,
        isLast = isLast,
        lineColor = tint,
    ) {
        YatteText(
            text = history.title,
            style = YatteTheme.typography.titleMedium,
            color = YatteTheme.colors.onSurface
        )
    }
}
