package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.*
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

/**
 * テーマ選択コンポーネント
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelector(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
) {
    ListItem(
        headlineContent = { Text(stringResource(SettingsRes.string.theme_title)) },
        supportingContent = {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = YatteSpacing.xs),
            ) {
                ThemeMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = selectedMode == mode,
                        onClick = { onModeSelected(mode) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ThemeMode.entries.size,
                        ),
                        modifier = Modifier.bounceClick(),
                    ) {
                        Text(mode.toDisplayString())
                    }
                }
            }
        },
    )
}

/**
 * テーマモードを日本語に変換
 */
@Composable
private fun ThemeMode.toDisplayString(): String = when (this) {
    ThemeMode.LIGHT -> stringResource(SettingsRes.string.theme_light)
    ThemeMode.DARK -> stringResource(SettingsRes.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(SettingsRes.string.theme_system)
}

