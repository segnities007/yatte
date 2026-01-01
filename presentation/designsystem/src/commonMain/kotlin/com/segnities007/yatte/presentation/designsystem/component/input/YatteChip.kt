package com.segnities007.yatte.presentation.designsystem.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.animation.rememberBounceInteraction
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Yatte統一チップコンポーネント（カスタム実装）
 * 
 * Material 3 FilterChipの代わりにBoxベースで実装
 * これによりBrush（グラデーション）を正しくサポート可能
 * 
 * 使用場面:
 * - 曜日選択
 * - カテゴリ選択
 * - フィルタリング
 */
@Composable
fun YatteChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    brush: Brush = YatteBrushes.Green.Main,
) {
    val shape = RoundedCornerShape(8.dp)
    val (interactionSource, bounceModifier) = rememberBounceInteraction()
    
    Box(
        modifier = modifier
            .then(bounceModifier)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.background(brush, shape)
                } else {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape)
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * カラーインジケーター付きチップ（カスタム実装）
 */
@Composable
fun YatteColorChip(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    brush: Brush = YatteBrushes.Green.Main,
) {
    val shape = RoundedCornerShape(8.dp)
    val (interactionSource, bounceModifier) = rememberBounceInteraction()
    
    Box(
        modifier = modifier
            .then(bounceModifier)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.background(brush, shape)
                } else {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline, shape)
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * 削除可能なチップ（カスタム実装）
 */
@Composable
fun YatteDismissibleChip(
    label: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    brush: Brush = YatteBrushes.Green.Main,
) {
    val shape = RoundedCornerShape(8.dp)
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush, shape)
            .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "削除",
                modifier = Modifier
                    .size(18.dp)
                    .bounceClick(onTap = onDismiss),
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteChipPreview() {
    var selected by remember { mutableStateOf(1) }
    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        YatteChip(label = "Mon", selected = selected == 0, onClick = { selected = 0 })
        YatteChip(label = "Tue", selected = selected == 1, onClick = { selected = 1 })
        YatteChip(label = "Wed", selected = selected == 2, onClick = { selected = 2 })
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteChipBrushVariantsPreview() {
    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        YatteChip(
            label = "Main",
            selected = true,
            onClick = {},
            brush = YatteBrushes.Green.Main
        )
        YatteChip(
            label = "Vivid",
            selected = true,
            onClick = {},
            brush = YatteBrushes.Green.Vivid
        )
        YatteChip(
            label = "Yellow",
            selected = true,
            onClick = {},
            brush = YatteBrushes.Yellow.Main
        )
    }
}

