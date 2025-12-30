package com.segnities007.yatte.presentation.designsystem.component.feedback

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.MotionDuration
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * Yatte標準ローディングインジケーター
 */
@Composable
fun YatteLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = color,
        strokeWidth = 4.dp,
    )
}

/**
 * メッセージ付きローディング
 */
@Composable
fun YatteLoadingWithMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        YatteLoadingIndicator()
        Spacer(modifier = Modifier.height(YatteSpacing.md))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * 3点ドットローディングアニメーション
 */
@Composable
fun YatteDotLoadingIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    dotColor: Color = MaterialTheme.colorScheme.primary,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot_loading")

    val scale1 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(MotionDuration.medium, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dot1",
    )
    val scale2 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(MotionDuration.medium, delayMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dot2",
    )
    val scale3 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(MotionDuration.medium, delayMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "dot3",
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.size(dotSize).scale(scale1).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(dotSize).scale(scale2).background(dotColor, CircleShape))
        Box(modifier = Modifier.size(dotSize).scale(scale3).background(dotColor, CircleShape))
    }
}

/**
 * シマーローディングエフェクト（スケルトン用）
 */
@Composable
fun YatteShimmerBox(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "shimmer_alpha",
    )

    Box(
        modifier = modifier
            .alpha(alpha)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(YatteSpacing.xs),
            ),
    )
}

@Preview(showBackground = true)
@Composable
private fun YatteLoadingPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        YatteLoadingIndicator()
        YatteLoadingWithMessage(message = "Loading data...")
        YatteDotLoadingIndicator()
        YatteShimmerBox(modifier = Modifier.fillMaxWidth().height(48.dp))
    }
}
