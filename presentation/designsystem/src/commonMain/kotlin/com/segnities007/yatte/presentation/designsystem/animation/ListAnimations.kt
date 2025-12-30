package com.segnities007.yatte.presentation.designsystem.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * リストアイテム用入場アニメーション
 */
object ListAnimations {
    /**
     * 上からスライドイン
     */
    fun slideInFromTop(durationMillis: Int = MotionDuration.medium): EnterTransition =
        slideInVertically(
            animationSpec = tween(durationMillis),
            initialOffsetY = { -it },
        ) + fadeIn(animationSpec = tween(durationMillis))

    /**
     * 下からスライドイン
     */
    fun slideInFromBottom(durationMillis: Int = MotionDuration.medium): EnterTransition =
        slideInVertically(
            animationSpec = tween(durationMillis),
            initialOffsetY = { it },
        ) + fadeIn(animationSpec = tween(durationMillis))

    /**
     * 左からスライドイン
     */
    fun slideInFromStart(durationMillis: Int = MotionDuration.medium): EnterTransition =
        slideInHorizontally(
            animationSpec = tween(durationMillis),
            initialOffsetX = { -it },
        ) + fadeIn(animationSpec = tween(durationMillis))

    /**
     * 右からスライドイン
     */
    fun slideInFromEnd(durationMillis: Int = MotionDuration.medium): EnterTransition =
        slideInHorizontally(
            animationSpec = tween(durationMillis),
            initialOffsetX = { it },
        ) + fadeIn(animationSpec = tween(durationMillis))

    /**
     * 展開アニメーション
     */
    fun expandIn(durationMillis: Int = MotionDuration.medium): EnterTransition =
        expandVertically(
            animationSpec = tween(durationMillis),
            expandFrom = Alignment.Top,
        ) + fadeIn(animationSpec = tween(durationMillis))

    /**
     * 上へスライドアウト
     */
    fun slideOutToTop(durationMillis: Int = MotionDuration.medium): ExitTransition =
        slideOutVertically(
            animationSpec = tween(durationMillis),
            targetOffsetY = { -it },
        ) + fadeOut(animationSpec = tween(durationMillis))

    /**
     * 下へスライドアウト
     */
    fun slideOutToBottom(durationMillis: Int = MotionDuration.medium): ExitTransition =
        slideOutVertically(
            animationSpec = tween(durationMillis),
            targetOffsetY = { it },
        ) + fadeOut(animationSpec = tween(durationMillis))

    /**
     * 収縮アニメーション
     */
    fun shrinkOut(durationMillis: Int = MotionDuration.medium): ExitTransition =
        shrinkVertically(
            animationSpec = tween(durationMillis),
            shrinkTowards = Alignment.Top,
        ) + fadeOut(animationSpec = tween(durationMillis))
}

/**
 * 遅延付きリストアイテムアニメーション
 */
@Composable
fun AnimatedListItem(
    visible: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
    baseDelay: Int = 50,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = MotionDuration.medium,
                delayMillis = index * baseDelay,
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = MotionDuration.medium,
                delayMillis = index * baseDelay,
            ),
            initialOffsetY = { it / 4 },
        ),
        exit = fadeOut(animationSpec = tween(MotionDuration.short)),
    ) {
        content()
    }
}
