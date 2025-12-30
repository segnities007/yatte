package com.segnities007.yatte.presentation.designsystem.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.rememberBounceInteraction

/**
 * Yatte統一アイコンボタン
 */
@Composable
fun YatteIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    size: Dp = 24.dp,
) {
    val (interactionSource, bounceModifier) = rememberBounceInteraction()

    IconButton(
        onClick = onClick,
        modifier = modifier.then(bounceModifier),
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
            tint = if (enabled) tint else tint.copy(alpha = 0.38f),
        )
    }
}

/**
 * 塗りつぶしアイコンボタン
 */
@Composable
fun YatteFilledIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    size: Dp = 24.dp,
) {
    val (interactionSource, bounceModifier) = rememberBounceInteraction()

    IconButton(
        onClick = onClick,
        modifier = modifier.then(bounceModifier),
        enabled = enabled,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
        )
    }
}

/**
 * トーン付きアイコンボタン
 */
@Composable
fun YatteTonalIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
    size: Dp = 24.dp,
) {
    val (interactionSource, bounceModifier) = rememberBounceInteraction()

    IconButton(
        onClick = onClick,
        modifier = modifier.then(bounceModifier),
        enabled = enabled,
        colors = IconButtonDefaults.filledTonalIconButtonColors(),
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteIconButtonPreview() {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        YatteIconButton(icon = Icons.AutoMirrored.Filled.ArrowBack, onClick = {}, contentDescription = "Back")
        YatteIconButton(icon = Icons.Default.Add, onClick = {}, contentDescription = "Add")
        YatteIconButton(icon = Icons.Default.Delete, onClick = {}, contentDescription = "Delete")
    }
}

