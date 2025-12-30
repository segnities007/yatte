package com.segnities007.yatte.presentation.designsystem.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput

object SpringSpecs {
    // Playful & Stress-free
    
    val PlayfulBounce = spring<Float>(
        stiffness = 400f,
        dampingRatio = 0.55f
    )

    // Standard interactive bounce
    val BouncyLow = spring<Float>(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioMediumBouncy
    )
    
    // Quick return or press
    val SnappyMedium = spring<Float>(
        stiffness = Spring.StiffnessMedium,
        dampingRatio = Spring.DampingRatioNoBouncy
    )
}

/**
 * 押し込みアニメーションを追加するModifier。
 * @param scaleDown 押し込み時の縮小率
 */
fun Modifier.bounceClick(
    scaleDown: Float = 0.95f,
    onTap: (() -> Unit)? = null
): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(scaleDown, SpringSpecs.SnappyMedium)
        } else {
            scale.animateTo(1f, SpringSpecs.PlayfulBounce)
        }
    }

    this
        .scale(scale.value)
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitFirstDown()
                    isPressed = true
                    val up = waitForUpOrCancellation()
                    isPressed = false
                    if (up != null) {
                        onTap?.invoke()
                    }
                }
            }
        }
}
