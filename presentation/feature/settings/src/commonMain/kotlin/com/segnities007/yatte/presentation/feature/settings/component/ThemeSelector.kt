package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeColor
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.component.list.YatteListItem
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.theme_blue
import yatte.presentation.feature.settings.generated.resources.theme_dark
import yatte.presentation.feature.settings.generated.resources.theme_default
import yatte.presentation.feature.settings.generated.resources.theme_green
import yatte.presentation.feature.settings.generated.resources.theme_light
import yatte.presentation.feature.settings.generated.resources.theme_orange
import yatte.presentation.feature.settings.generated.resources.theme_purple
import yatte.presentation.feature.settings.generated.resources.theme_system
import yatte.presentation.feature.settings.generated.resources.theme_title
import yatte.presentation.feature.settings.generated.resources.theme_yellow
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

/**
 * テーマモード選択コンポーネント
 */
@Composable
fun ThemeModeSelector(
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
 * テーマカラー選択コンポーネント
 */
@Composable
fun ThemeColorSelector(
    selectedColor: ThemeColor,
    onColorSelected: (ThemeColor) -> Unit,
) {
    YatteListItem(
        headlineContent = { YatteText("カラー") },
        supportingContent = {
            YatteSegmentedButtonRow(
                options = ThemeColor.entries.toList(),
                selectedIndex = ThemeColor.entries.indexOf(selectedColor),
                onOptionSelected = { _, color -> onColorSelected(color) },
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
}

/**
 * テーマカラーを日本語に変換
 */
@Composable
private fun ThemeColor.toDisplayString(): String = when (this) {
    ThemeColor.DEFAULT -> stringResource(SettingsRes.string.theme_default)
    ThemeColor.GREEN -> stringResource(SettingsRes.string.theme_green)
    ThemeColor.YELLOW -> stringResource(SettingsRes.string.theme_yellow)
    ThemeColor.BLUE -> stringResource(SettingsRes.string.theme_blue)
    ThemeColor.ORANGE -> stringResource(SettingsRes.string.theme_orange)
    ThemeColor.PURPLE -> stringResource(SettingsRes.string.theme_purple)
}


