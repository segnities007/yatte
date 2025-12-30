package com.segnities007.yatte.presentation.designsystem.component.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.rememberStrongBounceInteraction
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors

/**
 * Yatte統一FAB
 */
@Composable
fun YatteFloatingActionButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Add,
) {
    val (interactionSource, bounceModifier) = rememberStrongBounceInteraction()

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(initialScale = 0.6f) + fadeIn(),
        exit = scaleOut(targetScale = 0.6f) + fadeOut(),
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            FloatingActionButton(
                onClick = onClick,
                modifier = bounceModifier
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = YatteColors.sunshine.copy(alpha = 0.4f),
                        spotColor = YatteColors.honey.copy(alpha = 0.6f),
                    ),
                containerColor = YatteColors.sunshine,
                contentColor = Color(0xFF3E2723), // Dark brown for contrast
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp,
                    hoveredElevation = 12.dp,
                ),
                interactionSource = interactionSource,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(28.dp),
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



