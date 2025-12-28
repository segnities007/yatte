package com.segnities007.yatte.presentation.designsystem.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * パルスアニメーション
 *
 * 要素を周期的に拡大縮小させて注目を集める
 */
fun Modifier.pulseAnimation(
    minScale: Float = 0.95f,
    maxScale: Float = 1.05f,
    durationMillis: Int = 800,
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse_scale",
    )
    this.scale(scale)
}

/**
 * フェードパルスアニメーション
 *
 * 要素の透明度を周期的に変化させる
 */
fun Modifier.fadeAnimation(
    minAlpha: Float = 0.5f,
    maxAlpha: Float = 1f,
    durationMillis: Int = 1000,
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "fade")
    val alpha by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "fade_alpha",
    )
    this.alpha(alpha)
}

/**
 * シマーエフェクト
 *
 * ローディング状態を示すキラキラ効果
 */
fun Modifier.shimmerEffect(
    shimmerColor: Color = Color.White.copy(alpha = 0.3f),
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f),
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_progress",
    )
    
    val startOffset = shimmerProgress * 3f - 1f
    val endOffset = shimmerProgress * 3f
    
    this.background(
        brush = Brush.linearGradient(
            colors = listOf(
                backgroundColor,
                shimmerColor,
                backgroundColor,
            ),
            start = Offset(startOffset * 1000f, 0f),
            end = Offset(endOffset * 1000f, 0f),
        )
    )
}

/**
 * 呼吸アニメーション（ヘルプやヒント用）
 */
fun Modifier.breathingAnimation(
    durationMillis: Int = 2000,
): Modifier = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathing_scale",
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathing_alpha",
    )
    this.scale(scale).alpha(alpha)
}
