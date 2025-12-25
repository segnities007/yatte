package com.segnities007.yatte.presentation.feature.home.component

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
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

/**
 * タスクが空の時に表示するビュー
 */
@Composable
fun EmptyTasksView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(HomeRes.string.empty_tasks_emoji),
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(HomeRes.string.empty_no_tasks),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
