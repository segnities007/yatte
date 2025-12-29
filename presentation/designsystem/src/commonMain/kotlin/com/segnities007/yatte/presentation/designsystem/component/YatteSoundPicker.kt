package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * カスタム通知音選択コンポーネント
 *
 * @param currentSoundUri 現在設定されているカスタム音源のURI（nullの場合はデフォルト）
 * @param onSelectSound 音源選択ボタンがクリックされた時のコールバック
 * @param onClearSound 音源クリアボタンがクリックされた時のコールバック
 */
@Composable
fun YatteSoundPicker(
    currentSoundUri: String?,
    onSelectSound: () -> Unit,
    onClearSound: () -> Unit,
    title: String,
    selectedText: String,
    defaultText: String,
    selectButtonText: String,
    clearContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = YatteSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = if (currentSoundUri != null) {
                    selectedText
                } else {
                    defaultText
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = onSelectSound) {
                Text(selectButtonText)
            }
            if (currentSoundUri != null) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onClearSound,
                    modifier = Modifier.bounceClick()
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = clearContentDescription,
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
