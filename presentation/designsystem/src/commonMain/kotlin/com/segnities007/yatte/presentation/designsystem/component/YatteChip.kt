package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * Yatte統一チップコンポーネント
 *
 * カテゴリ選択やフィルタリングに使用
 */
@Composable
fun YatteChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier.bounceClick(),
        enabled = enabled,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        } else if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        } else null,
        shape = RoundedCornerShape(YatteSpacing.md),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )
}

/**
 * カラーインジケーター付きチップ
 *
 * カテゴリ表示などに使用
 */
@Composable
fun YatteColorChip(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(
                    text = label,
                    modifier = Modifier.padding(start = YatteSpacing.xs),
                )
            }
        },
        modifier = modifier.bounceClick(),
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        } else null,
        shape = RoundedCornerShape(YatteSpacing.md),
    )
}

/**
 * 削除可能なチップ
 */
@Composable
fun YatteDismissibleChip(
    label: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
) {
    FilterChip(
        selected = true,
        onClick = { },
        label = { Text(label) },
        modifier = modifier,
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        } else null,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "削除",
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = onDismiss),
            )
        },
        shape = RoundedCornerShape(YatteSpacing.md),
    )
}
