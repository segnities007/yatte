package com.segnities007.yatte.presentation.core.component

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

/**
 * 共通のフローチングヘッダーバー
 *
 * @param title タイトル（中央配置）
 * @param navigationIcon 左側のアイコン（戻るボタンなど）。nullの場合は非表示。
 * @param actions 右側のアクションボタン群。
 * @param modifier Modifier
 */
object FloatingHeaderBarDefaults {
    val ContainerHeight = 64.dp
    val TopMargin = 16.dp
    val BottomSpacing = 16.dp
    val HorizontalMargin = 24.dp
}

@Composable
fun FloatingHeaderBar(
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
        Surface(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp) // 画面端からのマージン (FNBと合わせる)
                .heightIn(min = FloatingHeaderBarDefaults.ContainerHeight), // 最小高さを設定
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                // Navigation Icon
                if (navigationIcon != null) {
                    navigationIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Spacer(modifier = Modifier.width(12.dp)) // アイコンがない場合の左余白確保
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

                if (actions == {}) { // actionsが空の場合の右余白
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
    }
}
