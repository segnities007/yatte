package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * 共通のフローチングヘッダーバー
 *
 * @param title タイトル（中央配置）
 * @param navigationIcon 左側のアイコン（戻るボタンなど）。nullの場合は非表示。
 * @param actions 右側のアクションボタン群。
 * @param modifier Modifier
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
            navigationIcon = navigationIcon,
            actions = actions,
        )
    }
}

@Composable
private fun YatteFloatingHeaderContent(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Surface(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = YatteSpacing.md, start = YatteSpacing.lg, end = YatteSpacing.lg)
                .heightIn(min = YatteFloatingHeaderDefaults.ContainerHeight),
            shape = RoundedCornerShape(YatteSpacing.xl),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = YatteSpacing.xs, vertical = YatteSpacing.xs),
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
                    ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
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

