package com.segnities007.yatte.presentation.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.presentation.designsystem.component.card.YatteCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * タイムライン用のカードコンポーネント
 * 履歴画面などで使用
 */
@Composable
fun YatteTimelineCard(
    time: String,
    lineColor: Color,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
    accentColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit,
) {
    YatteTimelineRow(
        time = time,
        lineColor = lineColor,
        isFirst = isFirst,
        isLast = isLast,
        modifier = modifier
    ) {
        YatteCard(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = YatteSpacing.xs),
            onClick = onClick,
            accentColor = accentColor,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(YatteSpacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}
