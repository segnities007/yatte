package com.segnities007.yatte.presentation.core.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick

/**
 * カスタム通知音選択コンポーネント
 *
 * @param currentSoundUri 現在設定されているカスタム音源のURI（nullの場合はデフォルト）
 * @param onSelectSound 音源選択ボタンがクリックされた時のコールバック
 * @param onClearSound 音源クリアボタンがクリックされた時のコールバック
 */
@Composable
fun SoundPicker(
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
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        headlineContent = {
            Text(title)
        },
        supportingContent = {
            Column {
                Text(
                    text = if (currentSoundUri != null) {
                        selectedText
                    } else {
                        defaultText
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
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
        },
    )
}
