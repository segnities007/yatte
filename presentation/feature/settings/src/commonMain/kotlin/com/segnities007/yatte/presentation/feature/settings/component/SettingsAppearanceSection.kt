package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.settings.SettingsIntent
import com.segnities007.yatte.presentation.feature.settings.SettingsState
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.section_appearance
import yatte.presentation.feature.settings.generated.resources.theme_dark
import yatte.presentation.feature.settings.generated.resources.theme_light
import yatte.presentation.feature.settings.generated.resources.theme_system
import yatte.presentation.feature.settings.generated.resources.theme_title
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@Composable
fun SettingsAppearanceSection(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
    YatteSectionCard(
        icon = Icons.Default.Palette,
        title = stringResource(SettingsRes.string.section_appearance),
    ) {
        YatteText(
            text = stringResource(SettingsRes.string.theme_title),
            style = YatteTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(YatteSpacing.sm))
        YatteSegmentedButtonRow(
            options = ThemeMode.entries.toList(),
            selectedIndex = ThemeMode.entries.indexOf(state.settings.themeMode),
            onOptionSelected = { _, mode -> onIntent(SettingsIntent.UpdateThemeMode(mode)) },
            content = { YatteText(it.toUiLabel()) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ThemeMode.toUiLabel(): String = when (this) {
    ThemeMode.LIGHT -> stringResource(SettingsRes.string.theme_light)
    ThemeMode.DARK -> stringResource(SettingsRes.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(SettingsRes.string.theme_system)
    ThemeMode.GREEN -> "グリーン"
    ThemeMode.YELLOW -> "イエロー"
}

@Composable
@Preview
fun SettingsAppearanceSectionPreview() {
    YatteTheme {
        SettingsAppearanceSection(
            state = SettingsState(),
            onIntent = {},
        )
    }
}
