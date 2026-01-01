package com.segnities007.yatte.presentation.designsystem.component.card

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
import com.segnities007.yatte.presentation.designsystem.component.display.YatteIcon
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
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
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiEffect
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiParticle
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * 特殊なアニメーション（紙吹雪）を持つインタラクティブなカード
 */
@Composable
fun YatteBouncyCard(
    title: String,
    supportingText: String? = null,
    isCompleted: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isChecked by remember { mutableStateOf(isCompleted) }
    
    // Confetti State
    val confettis = remember { mutableStateListOf<ConfettiParticle>() }
    val primaryColor = YatteTheme.colors.primary
    val secondaryColor = YatteTheme.colors.secondary

    YatteCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
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
                    .background(if (isChecked) primaryColor else YatteTheme.colors.onSurface.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    YatteIcon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = YatteTheme.colors.onPrimary,
                        modifier = Modifier.size(YatteSpacing.md)
                    )
                }
            }

            Spacer(modifier = Modifier.width(YatteSpacing.md))

            Column {
                YatteText(
                    text = title,
                    color = if (isChecked) YatteTheme.colors.onSurfaceVariant else YatteTheme.colors.onSurface,
                    style = YatteTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (supportingText != null) {
                    YatteText(
                        text = supportingText,
                        color = YatteTheme.colors.onSurfaceVariant,
                        style = YatteTheme.typography.bodyMedium
                    )
                }
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
