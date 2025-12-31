package com.segnities007.yatte.presentation.designsystem.component.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.SpringSpecs
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.designsystem.theme.YatteColors
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

data class YatteNavigationItem(
    val icon: ImageVector,
    val label: String,
    val isSelected: Boolean,
    val onClick: () -> Unit,
)

/**
 * フローティングナビゲーションバー
 */
private object YatteFloatingNavigationDefaults {
    val ContainerHeight = 72.dp
    val ContainerShape = CircleShape
    val ContainerHorizontalMargin = YatteSpacing.lg
    val ContainerVerticalMargin = YatteSpacing.md
    val ItemIconSize = 28.dp
    val ItemButtonWidth = 72.dp
    val ItemButtonHeight = 48.dp
    val SelectedItemButtonWidth = 88.dp
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
        shadowElevation = 8.dp,
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(YatteFloatingNavigationDefaults.ContainerHeight)
                .padding(horizontal = YatteSpacing.sm),
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
    // Animated pill width - expands when selected
    val buttonWidth by animateDpAsState(
        targetValue = if (item.isSelected) {
            YatteFloatingNavigationDefaults.SelectedItemButtonWidth
        } else {
            YatteFloatingNavigationDefaults.ItemButtonWidth
        },
        animationSpec = spring(
            stiffness = 400f,
            dampingRatio = 0.6f
        ),
        label = "navItemWidth",
    )

    // Animated scale - pops when selected
    val iconScale by animateFloatAsState(
        targetValue = if (item.isSelected) 1.1f else 1.0f,
        animationSpec = SpringSpecs.PlayfulBounce,
        label = "navItemScale",
    )

    val contentColor by animateColorAsState(
        targetValue = if (item.isSelected) {
            Color.White // Force White for Green Gradient
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "navItemContent",
    )

    // Pill-shaped container with gradient when selected
    Box(
        modifier = modifier
            .width(buttonWidth)
            .height(YatteFloatingNavigationDefaults.ItemButtonHeight)
            .clip(CircleShape)
            .bounceClick(onTap = item.onClick)
            .then(
                if (item.isSelected) {
                    Modifier.background(
                        brush = YatteBrushes.Green.Main
                    )
                } else {
                    Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                }
            )
            .padding(YatteSpacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier
                .size(YatteFloatingNavigationDefaults.ItemIconSize)
                .scale(iconScale),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteFloatingNavigationPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val items = listOf(
        YatteNavigationItem(Icons.Default.Home, "Home", selectedIndex == 0) { selectedIndex = 0 },
        YatteNavigationItem(Icons.Default.Settings, "Settings", selectedIndex == 1) { selectedIndex = 1 },
    )
    YatteFloatingNavigation(items = items)
}

