package com.segnities007.yatte.presentation.designsystem.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.presentation.designsystem.theme.PrimaryLight
import com.segnities007.yatte.presentation.designsystem.theme.SecondaryLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

object ConfettiManager {
    private val _paticles = mutableStateListOf<ConfettiParticle>()
    val particles: List<ConfettiParticle> get() = _paticles

    fun burst(position: Offset = Offset(500f, 1000f)) {
        // Simple burst effect
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            repeat(20) {
                _paticles.add(
                    ConfettiParticle(
                        color = if (Random.nextBoolean()) PrimaryLight else SecondaryLight,
                        angle = Random.nextFloat() * 360f,
                        startPosition = position // Using provided position or default
                    )
                )
            }
            delay(2000)
            _paticles.clear()
        }
    }
}

@Composable
fun ConfettiHost(
    modifier: Modifier = Modifier
) {
    if (ConfettiManager.particles.isNotEmpty()) {
        ConfettiEffect(ConfettiManager.particles, modifier)
    }
}
