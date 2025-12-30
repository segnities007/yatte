package com.segnities007.yatte.presentation.designsystem.component.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * スイッチのアクセントカラー
 */
enum class YatteSwitchAccent {
    /** デフォルト（プライマリ緑） */
    Default,
    /** 通知関連（オレンジ） */
    Notification,
    /** バイブレーション（青） */
    Vibration,
    /** サウンド（紫） */
    Sound,
}

/**
 * Yatte統一スイッチコンポーネント
 */
@Composable
fun YatteSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: YatteSwitchAccent = YatteSwitchAccent.Default,
) {
    val (thumbColor, trackColor) = when (accent) {
        YatteSwitchAccent.Default -> Pair(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
        )
        YatteSwitchAccent.Notification -> Pair(
            Color(0xFFFF9800),
            Color(0xFFFFE0B2),
        )
        YatteSwitchAccent.Vibration -> Pair(
            Color(0xFF2196F3),
            Color(0xFFBBDEFB),
        )
        YatteSwitchAccent.Sound -> Pair(
            Color(0xFF9C27B0),
            Color(0xFFE1BEE7),
        )
    }

    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = thumbColor,
            checkedTrackColor = trackColor,
            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun YatteSwitchPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Default", modifier = Modifier.width(100.dp))
            YatteSwitch(checked = true, onCheckedChange = {}, accent = YatteSwitchAccent.Default)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sound", modifier = Modifier.width(100.dp))
            YatteSwitch(checked = true, onCheckedChange = {}, accent = YatteSwitchAccent.Sound)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Vibration", modifier = Modifier.width(100.dp))
            YatteSwitch(checked = true, onCheckedChange = {}, accent = YatteSwitchAccent.Vibration)
        }
    }
}
