package com.segnities007.yatte.presentation.core.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * アプリ共通の空状態表示コンポーネント
 *
 * @param emoji 表示する絵文字 (例: "🎉", "📋")
 * @param message 表示するメッセージ
 */
@Composable
fun AppEmptyState(
    emoji: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(YatteSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.height(YatteSpacing.md))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

