package com.segnities007.yatte.presentation.feature.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// --- Colors based on ui-design.md ---
val MockPrimaryColor = Color(0xFF66BB6A) // Soft Green
val MockSecondaryColor = Color(0xFFFFF176) // Soft Yellow
val MockBackgroundColor = Color(0xFFFAFAFA) // Off White
val MockSurfaceColor = Color(0xFFFFFFFF)
val MockTextColor = Color(0xFF212121)

@Composable
fun MockDesignScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MockBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Text(
                text = "Today",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = MockTextColor,
                modifier = Modifier.padding(top = 24.dp)
            )

            // Bouncy Card List
            BouncyCard(
                title = "牛乳を買う",
                time = "10:00",
                isCompleted = false
            )

            BouncyCard(
                title = "ミーティング",
                time = "14:00",
                isCompleted = true
            )

            // Interactive Button
            BouncyButton(
                text = "Tap to Feel Physics",
                onClick = { /* Haptic & Sound would play here */ }
            )
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            BouncyFAB(onClick = {})
        }
    }
}

// --- Components with "Nintendo Quality" Physics ---

@Composable
fun BouncyCard(
    title: String,
    time: String,
    isCompleted: Boolean
) {
    var isChecked by remember { mutableStateOf(isCompleted) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    
    // Confetti State
    val confettis = remember { mutableStateListOf<ConfettiParticle>() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .bounceClick {
                 scope.launch {
                     scale.animateTo(0.95f, spring(stiffness = Spring.StiffnessMedium))
                     scale.animateTo(1f, spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy))
                 }
            }
            .clip(RoundedCornerShape(24.dp)) // "Squircle-ish" large radius
            .background(MockSurfaceColor)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Checkbox with "Pop" effect
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isChecked) MockPrimaryColor else Color.LightGray.copy(alpha = 0.3f))
                    .clickable {
                        isChecked = !isChecked
                        if (isChecked) {
                            // Trigger Confetti
                            repeat(20) {
                                confettis.add(
                                    ConfettiParticle(
                                        color = if (Random.nextBoolean()) MockPrimaryColor else MockSecondaryColor,
                                        angle = Random.nextFloat() * 360f,
                                        startPosition = Offset(16f, 16f) // Relative to this Box? No, handling Canvas separately is better for demo
                                    )
                                )
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = if (isChecked) Color.Gray else MockTextColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    // Strikethrough logic would go here
                )
                Text(
                    text = time,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
        
        // Confetti Overlay
        if (confettis.isNotEmpty()) {
            ConfettiEffect(confettis)
            LaunchedEffect(confettis.size) {
                 delay(1000)
                 confettis.clear()
            }
        }
    }
}

@Composable
fun BouncyButton(
    text: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(0.9f, spring(stiffness = Spring.StiffnessMedium))
        } else {
            scale.animateTo(1f, spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown()
                        isPressed = true
                        val up = waitForUpOrCancellation()
                        isPressed = false
                        if (up != null) {
                            onClick()
                        }
                    }
                }
            }
            .clip(RoundedCornerShape(20.dp))
            .background(MockPrimaryColor)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BouncyFAB(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(0.8f, spring(stiffness = Spring.StiffnessMedium))
        } else {
            scale.animateTo(1f, spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioHighBouncy))
        }
    }

    Box(
        modifier = Modifier
            .size(64.dp)
            .scale(scale.value)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown()
                        isPressed = true
                        val up = waitForUpOrCancellation()
                        isPressed = false
                        if (up != null) onClick()
                    }
                }
            }
            .clip(RoundedCornerShape(24.dp)) // Squircle-like
            .background(MockSecondaryColor)
            ,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MockTextColor,
            modifier = Modifier.size(32.dp)
        )
    }
}

// --- Utils ---

fun Modifier.bounceClick(onTap: () -> Unit = {}): Modifier = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(0.95f, spring(stiffness = Spring.StiffnessMedium))
        } else {
            scale.animateTo(1f, spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy))
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
                    if (up != null) onTap()
                }
            }
        }
}

data class ConfettiParticle(
    val color: Color,
    val angle: Float,
    var startPosition: Offset,
    val speed: Float = Random.nextFloat() * 20f + 10f
)

@Composable
fun ConfettiEffect(particles: List<ConfettiParticle>) {
    val progress = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        progress.animateTo(1f, spring(stiffness = Spring.StiffnessLow))
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val rad = p.angle * (Math.PI / 180)
            val dist = progress.value * 100f * p.speed * 0.1f
            val x = p.startPosition.x + cos(rad).toFloat() * dist
            val y = p.startPosition.y + sin(rad).toFloat() * dist
            
            drawCircle(
                color = p.color.copy(alpha = 1f - progress.value),
                radius = 8.dp.toPx() * (1f - progress.value),
                center = Offset(size.width / 2 + x, size.height / 2 + y) // Center burst
            )
        }
    }
}
