package com.segnities007.yatte.presentation.designsystem.effect

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.SpringSpecs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class ConfettiParticle(
    val color: Color,
    val angle: Float,
    var startPosition: Offset,
    val speed: Float = Random.nextFloat() * 20f + 10f
)

@Composable
fun ConfettiEffect(
    particles: List<ConfettiParticle>,
    modifier: Modifier = Modifier,
) {
    if (particles.isEmpty()) return

    val progress = remember { Animatable(0f) }

    LaunchedEffect(particles) {
        progress.snapTo(0f)
        progress.animateTo(1f, SpringSpecs.BouncyLow)
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { p ->
            val rad = p.angle * (Math.PI / 180)
            // Distances and logic adapted from Mock
            val dist = progress.value * 100f * p.speed * 0.1f
            val x = p.startPosition.x + cos(rad).toFloat() * dist
            val y = p.startPosition.y + sin(rad).toFloat() * dist

            drawCircle(
                color = p.color.copy(alpha = 1f - progress.value),
                radius = 8.dp.toPx() * (1f - progress.value),
                center = Offset(size.width / 2 + x, size.height / 2 + y) // Center burst relative to Canvas? Need adjustments for positioning.
                // In Mock, offset was relative to center. Let's assume startPosition is relative to screen center for now or adjust.
            )
        }
    }
}
