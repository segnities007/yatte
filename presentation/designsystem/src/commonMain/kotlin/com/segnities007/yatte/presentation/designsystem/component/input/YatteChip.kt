package com.segnities007.yatte.presentation.designsystem.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.animation.rememberBounceInteraction
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * Yatte統一チップコンポーネント
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
    val (interactionSource, bounceModifier) = rememberBounceInteraction()

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier.then(bounceModifier),
        enabled = enabled,
        interactionSource = interactionSource,
        leadingIcon = if (selected) {
            { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
        } else if (leadingIcon != null) {
            { Icon(imageVector = leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp)) }
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
 */
@Composable
fun YatteColorChip(
    label: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (interactionSource, bounceModifier) = rememberBounceInteraction()

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
                Text(text = label, modifier = Modifier.padding(start = YatteSpacing.xs))
            }
        },
        modifier = modifier.then(bounceModifier),
        interactionSource = interactionSource,
        leadingIcon = if (selected) {
            { Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
        } else null,
        shape = RoundedCornerShape(YatteSpacing.md),
    )
}

/**
 * 削除可能なチップ
 * Note: trailingIconはInteractionSourceを持たないため、bounceClickを維持
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
            { Icon(imageVector = leadingIcon, contentDescription = null, modifier = Modifier.size(18.dp)) }
        } else null,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "削除",
                modifier = Modifier.size(18.dp).bounceClick(onTap = onDismiss),
            )
        },
        shape = RoundedCornerShape(YatteSpacing.md),
    )
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

