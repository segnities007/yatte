package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick

@Composable
fun YatteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true,
) {
    // Note: Button Composable already handles clicks, so we apply bounceClick to the Modifier
    // But standard Button consumes touch events. We might need to wrap content or customize interaction source.
    // For simplicity with bounceClick, we can apply it to the Modifier passed to Button, 
    // but the standard Button ripple might conflict visually.
    // However, Mock implemented explicit handling. Let's use the bounceClick modifier on the button itself.
    
    Button(
        onClick = onClick,
        modifier = modifier.bounceClick(scaleDown = 0.95f), // Apply bounce effect
        shape = RoundedCornerShape(16.dp), // Squircle-ish
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
