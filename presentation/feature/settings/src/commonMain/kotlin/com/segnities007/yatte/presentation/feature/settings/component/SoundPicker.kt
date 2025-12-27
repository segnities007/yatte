package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.*
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes
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
            Text(stringResource(SettingsRes.string.custom_sound_title))
        },
        supportingContent = {
            Column {
                Text(
                    text = if (currentSoundUri != null) {
                        stringResource(SettingsRes.string.custom_sound_selected)
                    } else {
                        stringResource(SettingsRes.string.custom_sound_default)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(onClick = onSelectSound) {
                        Text(stringResource(SettingsRes.string.custom_sound_select))
                    }
                    if (currentSoundUri != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onClearSound,
                            modifier = Modifier.bounceClick()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(SettingsRes.string.custom_sound_clear),
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        },
    )
}
