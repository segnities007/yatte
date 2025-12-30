package com.segnities007.yatte.presentation.designsystem.animation

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset

/**
 * Yatteアプリのアニメーション時間トークン
 */
object MotionDuration {
    /** 100ms - 非常に短い（micro-interactions） */
    const val instant: Int = 100
    /** 150ms - 短い（ボタンプレス、チェックボックス） */
    const val short: Int = 150
    /** 250ms - 標準（フェード、スライド） */
    const val medium: Int = 250
    /** 400ms - 長い（モーダル表示/非表示） */
    const val long: Int = 400
    /** 600ms - 強調（画面遷移、大きなアニメーション） */
    const val emphasis: Int = 600
}

/**
 * Yatteアプリのイージング定義
 */
object MotionEasing {
    /** 標準的な減速イージング（進入時） */
    val decelerate = FastOutSlowInEasing
    /** 標準的な加速イージング（退出時） */
    val accelerate = FastOutLinearInEasing
    /** 線形 */
    val linear = LinearEasing
    /** 強調イージング */
    val emphasized = EaseInOutCubic
}

/**
 * Springアニメーション定義
 */
object MotionSpring {
    /** 弾むようなスプリング（低剛性） */
    val bouncy: FiniteAnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioMediumBouncy,
    )
    /** 高い弾み */
    val bouncyHigh: FiniteAnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioHighBouncy,
    )
    /** スナップ感のあるスプリング（バウンスなし） */
    val snappy: FiniteAnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessMedium,
        dampingRatio = Spring.DampingRatioNoBouncy,
    )
    /** 高速スナップ */
    val snappyFast: FiniteAnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessHigh,
        dampingRatio = Spring.DampingRatioNoBouncy,
    )
    /** ジェントルなスプリング */
    val gentle: FiniteAnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessVeryLow,
        dampingRatio = Spring.DampingRatioLowBouncy,
    )
}

/**
 * IntOffsetアニメーション用スプリング
 */
object MotionSpringOffset {
    val bouncy: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioMediumBouncy,
    )
    val snappy: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMedium,
        dampingRatio = Spring.DampingRatioNoBouncy,
    )
}

/**
 * プリセットTweenアニメーション
 */
object MotionTween {
    fun <T> short(): FiniteAnimationSpec<T> = tween(
        durationMillis = MotionDuration.short,
        easing = MotionEasing.decelerate,
    )
    fun <T> medium(): FiniteAnimationSpec<T> = tween(
        durationMillis = MotionDuration.medium,
        easing = MotionEasing.decelerate,
    )
    fun <T> long(): FiniteAnimationSpec<T> = tween(
        durationMillis = MotionDuration.long,
        easing = MotionEasing.emphasized,
    )
}
