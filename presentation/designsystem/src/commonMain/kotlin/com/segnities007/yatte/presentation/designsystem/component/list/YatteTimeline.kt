package com.segnities007.yatte.presentation.designsystem.component.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun YatteTimeline(
    color: Color,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    lineWidth: Dp = 2.dp,
    dotRadius: Dp = 6.dp,
    width: Dp = 24.dp,
) {
    Box(
        modifier = modifier
            .width(width)
            .fillMaxHeight(),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val dotRadiusPx = dotRadius.toPx()
            val lineWidthPx = lineWidth.toPx()

            // Top line
            if (!isFirst) {
                drawLine(
                    color = color.copy(alpha = 0.5f),
                    start = Offset(centerX, 0f),
                    end = Offset(centerX, centerY),
                    strokeWidth = lineWidthPx
                )
            }

            // Bottom line
            if (!isLast) {
                drawLine(
                    color = color.copy(alpha = 0.5f),
                    start = Offset(centerX, centerY),
                    end = Offset(centerX, size.height),
                    strokeWidth = lineWidthPx
                )
            }

            // Dot
            drawCircle(
                color = color,
                radius = dotRadiusPx,
                center = Offset(centerX, centerY)
            )
        }
    }
}

@Preview
@Composable
private fun YatteTimelinePreview() {
    YatteTimeline(color = Color.Blue, isFirst = false, isLast = false)
}
