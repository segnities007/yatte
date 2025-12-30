package com.segnities007.yatte.presentation.designsystem.component.list

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

private val TimeColumnWidth = 56.dp
private val TimelineColumnWidth = 28.dp

@Composable
fun YatteTimelineRow(
    time: String,
    lineColor: Color,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = YatteSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time Display
        Text(
            text = time,
            style = MaterialTheme.typography.labelLarge,
            color = lineColor,
            modifier = Modifier.width(TimeColumnWidth)
        )

        // Timeline (Vertical line and dot)
        YatteTimeline(
            color = lineColor,
            isFirst = isFirst,
            isLast = isLast,
            width = TimelineColumnWidth,
        )

        // Content (Card, etc.)
        content()
    }
}
