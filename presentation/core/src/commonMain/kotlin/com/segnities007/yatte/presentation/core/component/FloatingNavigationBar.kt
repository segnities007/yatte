package com.segnities007.yatte.presentation.core.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.*

/**
 * ナビゲーション項目
 */
enum class NavItem(
    val icon: ImageVector,
    val labelRes: StringResource,
) {
    HOME(Icons.Default.Home, Res.string.nav_home),
    MANAGE(Icons.AutoMirrored.Filled.List, Res.string.nav_manage),
    HISTORY(Icons.Default.History, Res.string.nav_history),
    SETTINGS(Icons.Default.Settings, Res.string.nav_settings),
}

/**
 * フローティングナビゲーションバー
 * 画面下部に浮かぶ形式のナビゲーション（FABは別途配置）
 */
private object FnbLayoutDefaults {
    val ContainerHeight = 72.dp
    val ContainerShape = RoundedCornerShape(YatteSpacing.xl)
    val ContainerHorizontalMargin = YatteSpacing.lg
    val ContainerVerticalMargin = YatteSpacing.md
    val ItemIconSize = 28.dp
    val ItemButtonSize = 56.dp
    const val ContainerAlpha = 1.0f
}

@Composable
fun FloatingNavigationBar(
    currentItem: NavItem,
    onItemSelected: (NavItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = FnbLayoutDefaults.ContainerHorizontalMargin,
                vertical = FnbLayoutDefaults.ContainerVerticalMargin
            ),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = FnbLayoutDefaults.ContainerShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FnbLayoutDefaults.ContainerHeight)
                    .padding(horizontal = YatteSpacing.xs),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NavItem.entries.forEach { item ->
                    NavItemButton(
                        item = item,
                        isSelected = currentItem == item,
                        onClick = { onItemSelected(item) },
                    )
                }
            }
        }
    }
}

@Composable
private fun NavItemButton(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = stringResource(item.labelRes)
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
        },
        label = "navItemBackground",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "navItemContent",
    )

    Box(
        modifier = modifier
            .size(FnbLayoutDefaults.ItemButtonSize)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .bounceClick(onTap = onClick)
            .background(backgroundColor)
            .padding(YatteSpacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(FnbLayoutDefaults.ItemIconSize),
        )
    }
}
