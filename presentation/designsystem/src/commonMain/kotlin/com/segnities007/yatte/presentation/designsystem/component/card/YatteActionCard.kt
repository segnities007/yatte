package com.segnities007.yatte.presentation.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.component.display.YatteIcon
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme

/**
 * アクション可能な汎用カード
 * タスクカード、プロジェクトカードなどのベースとして使用
 */
@Composable
fun YatteActionCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color? = null,
    elevation: Dp = 2.dp,
    onDismiss: (() -> Unit)? = null,
    supportingContent: @Composable (ColumnScope.() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    if (onDismiss != null) {
        val dismissState = rememberSwipeToDismissBoxState()

        LaunchedEffect(dismissState.currentValue) {
            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart ||
                dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd
            ) {
                onDismiss()
            }
        }

        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(YatteTheme.colors.errorContainer)
                        .padding(horizontal = YatteSpacing.lg),
                    contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    },
                ) {
                    YatteIcon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = YatteTheme.colors.onErrorContainer,
                    )
                }
            },
            content = {
                YatteCard(
                    onClick = onClick,
                    modifier = modifier.fillMaxWidth(),
                    elevation = elevation,
                    accentColor = accentColor,
                ) {
                    ActionCardRow(title, supportingContent, actions)
                }
            },
        )
    } else {
        YatteCard(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            elevation = elevation,
            accentColor = accentColor,
        ) {
            ActionCardRow(title, supportingContent, actions)
        }
    }
}

@Composable
private fun ActionCardRow(
    title: String,
    supportingContent: @Composable (ColumnScope.() -> Unit)?,
    actions: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(YatteSpacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            YatteText(
                text = title,
                style = YatteTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = YatteTheme.colors.onSurface
            )
            supportingContent?.invoke(this)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(YatteSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}
