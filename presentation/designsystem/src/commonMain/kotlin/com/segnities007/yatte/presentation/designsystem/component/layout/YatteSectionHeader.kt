package com.segnities007.yatte.presentation.designsystem.component.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * 仕様: 画面内のセクション見出し
 */
@Composable
fun YatteSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.padding(horizontal = YatteSpacing.md, vertical = YatteSpacing.xs),
        color = MaterialTheme.colorScheme.primary,
    )
}
