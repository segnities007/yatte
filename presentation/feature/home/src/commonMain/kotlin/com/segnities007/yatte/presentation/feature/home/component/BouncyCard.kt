package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiEffect
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiParticle
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun BouncyCard(
    title: String,
    time: String,
    isCompleted: Boolean,
    onClick: () -> Unit = {},
) {
    var isChecked by remember { mutableStateOf(isCompleted) }
    val scope = rememberCoroutineScope()
    
    // Confetti State
    val confettis = remember { mutableStateListOf<ConfettiParticle>() }
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    // Outer Card -> YatteCard handles the bounce
    YatteCard(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(YatteSpacing.lg)
        ) {
            Box(
                modifier = Modifier
                    .size(YatteSpacing.xl)
                    .bounceClick(onTap = {
                        isChecked = !isChecked
                        if (isChecked) {
                            repeat(20) {
                                confettis.add(
                                    ConfettiParticle(
                                        color = if (Random.nextBoolean()) primaryColor else secondaryColor,
                                        angle = Random.nextFloat() * 360f,
                                        startPosition = Offset(16f, 16f)
                                    )
                                )
                            }
                        }
                    })
                    .clip(RoundedCornerShape(YatteSpacing.sm))
                    .background(if (isChecked) primaryColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(YatteSpacing.md)
                    )
                }
            }

            Spacer(modifier = Modifier.width(YatteSpacing.md))

            Column {
                Text(
                    text = title,
                    color = if (isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = time,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
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
@Preview
fun BouncyCardPreview() {
    MaterialTheme {
        BouncyCard(
            title = "Bounce Task",
            time = "10:00",
            isCompleted = false,
        )
    }
}
