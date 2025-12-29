package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing


data class YatteNavigationItem(
    val icon: ImageVector,
    val label: String,
    val isSelected: Boolean,
    val onClick: () -> Unit,
)

/**
 * フローティングナビゲーションバー
 * 画面下部に浮かぶ形式のナビゲーション（FABは別途配置）
 */
private object YatteFloatingNavigationDefaults {
    val ContainerHeight = 72.dp
    val ContainerShape = RoundedCornerShape(YatteSpacing.xl)
    val ContainerHorizontalMargin = YatteSpacing.lg
    val ContainerVerticalMargin = YatteSpacing.md
    val ItemIconSize = 28.dp
    val ItemButtonSize = 56.dp
}

@Composable
fun YatteFloatingNavigation(
    items: List<YatteNavigationItem>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = YatteFloatingNavigationDefaults.ContainerHorizontalMargin,
                vertical = YatteFloatingNavigationDefaults.ContainerVerticalMargin
            ),
        contentAlignment = Alignment.Center,
    ) {
        YatteFloatingNavigationSurface {
            YatteFloatingNavigationItems(items = items)
        }
    }
}

@Composable
private fun YatteFloatingNavigationSurface(
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = YatteFloatingNavigationDefaults.ContainerShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(YatteFloatingNavigationDefaults.ContainerHeight)
                .padding(horizontal = YatteSpacing.xs),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
private fun YatteFloatingNavigationItems(
    items: List<YatteNavigationItem>,
) {
    items.forEach { item ->
        YatteNavItemButton(item = item)
    }
}

@Composable
private fun YatteNavItemButton(
    item: YatteNavigationItem,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (item.isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
        },
        label = "navItemBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = if (item.isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "navItemContent",
    )

    Box(
        modifier = modifier
            .size(YatteFloatingNavigationDefaults.ItemButtonSize)
            .clip(CircleShape)
            .bounceClick(onTap = item.onClick)
            .background(backgroundColor)
            .padding(YatteSpacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(YatteFloatingNavigationDefaults.ItemIconSize),
        )
    }
}
