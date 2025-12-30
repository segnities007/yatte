package com.segnities007.yatte.presentation.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.rememberBounceInteraction

/**
 * Yatte統一テキストボタンコンポーネント
 */
@Composable
fun YatteTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.primary,
) {
    val (interactionSource, bounceModifier) = rememberBounceInteraction()

    TextButton(
        onClick = onClick,
        modifier = modifier.then(bounceModifier),
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            color = if (enabled) textColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteTextButtonPreview() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        YatteTextButton(text = "Cancel", onClick = {})
        YatteTextButton(text = "Confirm", onClick = {})
    }
}

