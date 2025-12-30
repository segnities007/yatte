package com.segnities007.yatte.presentation.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

/**
 * バウンスインタラクションを提供するComposable
 * 
 * InteractionSourceとバウンスアニメーション用のModifierをセットで返す。
 * Material3コンポーネント（Button, FAB, IconButton等）と組み合わせて使用。
 * 
 * Usage:
 * ```kotlin
 * val (interactionSource, bounceModifier) = rememberBounceInteraction()
 * 
 * Button(
 *     onClick = onClick,
 *     modifier = modifier.then(bounceModifier),
 *     interactionSource = interactionSource,
 * ) { ... }
 * ```
 * 
 * @param scaleDown プレス時のスケールダウン率 (デフォルト: 0.95f)
 * @return Pair<MutableInteractionSource, Modifier> - InteractionSourceとバウンスModifier
 */
@Composable
fun rememberBounceInteraction(
    scaleDown: Float = 0.95f,
): Pair<MutableInteractionSource, Modifier> {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(scaleDown, SpringSpecs.SnappyMedium)
        } else {
            scale.animateTo(1f, SpringSpecs.PlayfulBounce)
        }
    }

    return interactionSource to Modifier.scale(scale.value)
}

/**
 * 強いバウンス効果（FAB向け）
 */
@Composable
fun rememberStrongBounceInteraction(): Pair<MutableInteractionSource, Modifier> {
    return rememberBounceInteraction(scaleDown = 0.90f)
}
