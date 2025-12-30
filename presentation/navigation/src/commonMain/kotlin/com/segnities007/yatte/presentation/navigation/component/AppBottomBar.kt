package com.segnities007.yatte.presentation.navigation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.component.navigation.YatteFloatingNavigation
import com.segnities007.yatte.presentation.designsystem.component.navigation.YatteNavigationItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBottomBar(
    isVisible: Boolean,
    currentItem: NavItem,
    onItemSelected: (NavItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut(),
        modifier = modifier,
    ) {
        val yatteItems = NavItem.entries.map { item ->
            YatteNavigationItem(
                icon = item.icon,
                label = stringResource(item.labelRes),
                isSelected = currentItem == item,
                onClick = { onItemSelected(item) }
            )
        }

        YatteFloatingNavigation(
            items = yatteItems,
        )
    }
}
