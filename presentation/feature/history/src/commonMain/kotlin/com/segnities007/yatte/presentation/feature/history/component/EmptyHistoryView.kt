package com.segnities007.yatte.presentation.feature.history.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.history.generated.resources.Res as HistoryRes

/**
 * 履歴が空の時に表示するビュー
 */
@Composable
fun EmptyHistoryView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(HistoryRes.string.common_empty_emoji),
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(HistoryRes.string.empty_no_history),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
