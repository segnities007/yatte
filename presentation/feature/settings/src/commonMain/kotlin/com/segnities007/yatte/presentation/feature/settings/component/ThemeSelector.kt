package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.component.list.YatteListItem
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.theme_dark
import yatte.presentation.feature.settings.generated.resources.theme_light
import yatte.presentation.feature.settings.generated.resources.theme_system
import yatte.presentation.feature.settings.generated.resources.theme_title
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

/**
 * テーマ選択コンポーネント
 */
@Composable
fun ThemeSelector(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
) {
    YatteListItem(
        headlineContent = { YatteText(stringResource(SettingsRes.string.theme_title)) },
        supportingContent = {
            YatteSegmentedButtonRow(
                options = ThemeMode.entries.toList(),
                selectedIndex = ThemeMode.entries.indexOf(selectedMode),
                onOptionSelected = { _, mode -> onModeSelected(mode) },
                content = { YatteText(it.toDisplayString()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = YatteSpacing.xs),
            )
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
    ThemeMode.GREEN -> "グリーン"
    ThemeMode.YELLOW -> "イエロー"
}
