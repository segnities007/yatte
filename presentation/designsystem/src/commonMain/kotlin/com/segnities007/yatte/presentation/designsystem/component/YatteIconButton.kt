package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick

/**
 * Yatte統一アイコンボタン
 *
 * bounceClickアニメーション付き
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
    IconButton(
        onClick = onClick,
        modifier = modifier.bounceClick(),
        enabled = enabled,
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
    IconButton(
        onClick = onClick,
        modifier = modifier.bounceClick(),
        enabled = enabled,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
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
    IconButton(
        onClick = onClick,
        modifier = modifier.bounceClick(),
        enabled = enabled,
        colors = IconButtonDefaults.filledTonalIconButtonColors(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
        )
    }
}
