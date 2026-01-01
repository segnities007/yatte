package com.segnities007.yatte.presentation.designsystem.effect

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random



/**
 * 汎用コンフェッティ演出
 */
@Composable
fun YatteConfettiEffect(particles: List<ConfettiParticle>) {
    val progress = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        progress.animateTo(1f, spring(stiffness = Spring.StiffnessLow))
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val rad = p.angle * (PI / 180)
            val dist = progress.value * 100f * p.speed * 0.1f
            val x = p.startPosition.x + cos(rad).toFloat() * dist
            val y = p.startPosition.y + sin(rad).toFloat() * dist
            
            drawCircle(
                color = p.color.copy(alpha = 1f - progress.value),
                radius = 8.dp.toPx() * (1f - progress.value),
                center = Offset(size.width / 2 + x, size.height / 2 + y)
            )
        }
    }
}
