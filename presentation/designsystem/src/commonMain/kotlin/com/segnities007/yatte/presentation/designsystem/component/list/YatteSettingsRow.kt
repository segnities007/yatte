package com.segnities007.yatte.presentation.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSlider
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSwitch
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSwitchAccent
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * スイッチ付き設定行 (Design System)
 */
@Composable
fun YatteSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    accent: YatteSwitchAccent = YatteSwitchAccent.Default,
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
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        YatteSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            accent = accent,
        )
    }
}

/**
 * スライダー付き設定行 (Design System)
 */
@Composable
fun YatteSliderRow(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..60f,
    steps: Int = 11,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = YatteSpacing.xs),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(YatteSpacing.xs))
        YatteSlider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = valueRange,
            steps = steps,
        )
    }
}

/**
 * アクションボタン付き設定行 (Design System)
 */
@Composable
fun YatteActionRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    action: @Composable () -> Unit,
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
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        action()
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun YatteSettingsRowsPreview() {
    MaterialTheme {
        Column {
            YatteSwitchRow(
                title = "Switch Row",
                subtitle = "Subtitle",
                checked = true,
                onCheckedChange = {}
            )
            YatteSliderRow(
                title = "Slider Row",
                value = 30,
                onValueChange = {}
            )
            YatteActionRow(
                title = "Action Row",
                subtitle = "With Button",
                action = { Text("Action") }
            )
        }
    }
}
