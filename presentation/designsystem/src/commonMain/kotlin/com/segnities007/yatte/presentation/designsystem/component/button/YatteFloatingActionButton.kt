package com.segnities007.yatte.presentation.designsystem.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.rememberStrongBounceInteraction
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Yatte統一FAB (カスタム実装)
 * 
 * Material 3 FloatingActionButtonの代わりにBoxベースで実装
 * これによりBrush（グラデーション）を正しくサポート
 */
@Composable
fun YatteFloatingActionButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
    brush: Brush = YatteBrushes.Yellow.Main, // Using Main for better visibility (Vivid definition is currently flat)
) {
    val (interactionSource, bounceModifier) = rememberStrongBounceInteraction()
    val shape = CircleShape

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally { it } + fadeIn(),
        exit = slideOutHorizontally { it } + fadeOut(),
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(16.dp)) { // Padding for shadow overlap area
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .then(bounceModifier)
                    .shadow(
                        elevation = 8.dp,
                        shape = shape,
                        ambientColor = YatteColors.sunshine.copy(alpha = 0.4f),
                        spotColor = YatteColors.honey.copy(alpha = 0.6f),
                    )
                    .clip(shape)
                    .background(brush)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = Color.White),
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF3E2723) // Dark Brown for contrast against Yellow
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteFloatingActionButtonPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        YatteFloatingActionButton(isVisible = true, onClick = {}, contentDescription = "Add")
    }
}



