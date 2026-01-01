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
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.core.file.FileHelper
import com.segnities007.yatte.presentation.core.file.rememberFileHelper
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import androidx.compose.runtime.rememberCoroutineScope
import com.segnities007.yatte.presentation.feature.settings.SettingsIntent
import androidx.compose.ui.unit.dp
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

@Composable
@Preview
fun SettingsContentPreview() {
    YatteTheme {
        val fileHelper = rememberFileHelper()
        val soundPickerLauncher = rememberSoundPickerLauncher {}
        val scope = rememberCoroutineScope()

        SettingsContent(
            state = SettingsState(),
            onIntent = {},
            onLicenseClick = {},

            fileHelper = fileHelper,
            soundPickerLauncher = soundPickerLauncher,
            scope = scope,
            contentPadding = PaddingValues(0.dp),
            onShowSnackbar = {},
        )
    }
}
