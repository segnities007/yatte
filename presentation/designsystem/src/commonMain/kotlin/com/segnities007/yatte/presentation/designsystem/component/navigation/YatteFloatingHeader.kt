package com.segnities007.yatte.presentation.designsystem.component.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import com.segnities007.yatte.presentation.designsystem.theme.LocalYattePrimaryBrush
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings

/**
 * フローティングヘッダーバー
 */
object YatteFloatingHeaderDefaults {
    val ContainerHeight = 64.dp
    val TopMargin = YatteSpacing.md
    val BottomSpacing = YatteSpacing.md
    val HorizontalMargin = YatteSpacing.lg
}

@Composable
fun YatteFloatingHeader(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    useGradient: Boolean = false,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier,
    ) {
        YatteFloatingHeaderContent(
            title = title,
            useGradient = useGradient,
            navigationIcon = navigationIcon,
            actions = actions,
        )
    }
}

@Composable
private fun YatteFloatingHeaderContent(
    title: @Composable () -> Unit,
    useGradient: Boolean = false,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val containerColor = if (useGradient) Color.Transparent else YatteTheme.colors.surface
    val contentColor = if (useGradient) Color.White else YatteTheme.colors.onSurface

    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = YatteSpacing.md, start = YatteSpacing.lg, end = YatteSpacing.lg)
            .heightIn(min = YatteFloatingHeaderDefaults.ContainerHeight)
            .shadow(
                elevation = 8.dp,
                shape = YatteTheme.shapes.extraLarge,
                ambientColor = YatteTheme.colors.primary.copy(alpha = 0.3f),
                spotColor = YatteTheme.colors.primary.copy(alpha = 0.4f),
            ),
        shape = YatteTheme.shapes.extraLarge,
        color = containerColor,
        shadowElevation = 0.dp, // We use custom shadow
        tonalElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .then(
                    if (useGradient) {
                    Modifier.background(brush = LocalYattePrimaryBrush.current)
                } else {
                    Modifier.background(YatteTheme.colors.surface)
                }
            )
            .padding(horizontal = YatteSpacing.xs, vertical = YatteSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // Navigation Icon
        if (navigationIcon != null) {
            navigationIcon()
            Spacer(modifier = Modifier.width(YatteSpacing.xs))
        } else {
            Spacer(modifier = Modifier.width(YatteSpacing.sm))
        }

        // Title (Weight 1f to push actions to right)
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ProvideTextStyle(
                value = YatteTheme.typography.titleMedium.copy(
                    color = contentColor
                )
            ) {
                title()
            }
        }

            // Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                actions()
            }

            // Balance spacing if no actions
            Spacer(modifier = Modifier.width(if (actions == {}) YatteSpacing.sm else 0.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YatteFloatingHeaderPreview() {
    YatteTheme {
        Column {
            YatteFloatingHeader(
                title = { YatteText("Header Title") },
                navigationIcon = {
                    YatteIconButton(
                        onClick = {},
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                    )
                },
                actions = {
                    YatteIconButton(
                        onClick = {},
                        icon = Icons.Default.Settings,
                    )
                }
            )
        }
    }
}


