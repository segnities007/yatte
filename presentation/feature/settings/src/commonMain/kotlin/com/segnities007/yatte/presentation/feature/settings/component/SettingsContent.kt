package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.core.file.FileHelper
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.settings.SettingsIntent
import com.segnities007.yatte.presentation.feature.settings.SettingsState
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingsContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onLicenseClick: () -> Unit,
    fileHelper: FileHelper,
    soundPickerLauncher: SoundPickerLauncher,
    scope: CoroutineScope,
    contentPadding: PaddingValues,
    onShowSnackbar: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
                start = YatteSpacing.md,
                end = YatteSpacing.md,
            ),
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.md),
    ) {
        SettingsNotificationSection(
            state = state,
            onIntent = onIntent,
            soundPickerLauncher = soundPickerLauncher
        )

        SettingsAppearanceSection(
            state = state,
            onIntent = onIntent
        )

        SettingsDataSection(
            onIntent = onIntent,
            onLicenseClick = onLicenseClick,
            fileHelper = fileHelper,
            scope = scope,
            onShowSnackbar = onShowSnackbar
        )
    }
}
