package com.segnities007.yatte.presentation.designsystem.component.layout

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
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * カスタム通知音選択コンポーネント
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

@Preview(showBackground = true)
@Composable
private fun YatteSoundPickerPreview() {
    Column {
        YatteSoundPicker(
            currentSoundUri = null,
            onSelectSound = {},
            onClearSound = {},
            title = "Notification Sound",
            selectedText = "Custom Sound.mp3",
            defaultText = "Default Sound",
            selectButtonText = "Select",
            clearContentDescription = "Clear Sound"
        )
        YatteSoundPicker(
            currentSoundUri = "content://media/external/audio/media/1234",
            onSelectSound = {},
            onClearSound = {},
            title = "Notification Sound",
            selectedText = "Custom Sound.mp3",
            defaultText = "Default Sound",
            selectButtonText = "Change",
            clearContentDescription = "Clear Sound"
        )
    }
}
