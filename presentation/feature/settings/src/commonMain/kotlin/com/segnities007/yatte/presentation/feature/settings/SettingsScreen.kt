package com.segnities007.yatte.presentation.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.file.FileHelper
import com.segnities007.yatte.presentation.core.file.rememberFileHelper
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteConfirmDialog
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.settings.component.SettingsNotificationSection
import com.segnities007.yatte.presentation.feature.settings.component.SettingsAppearanceSection
import com.segnities007.yatte.presentation.feature.settings.component.SettingsDataSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.nav_settings
import yatte.presentation.feature.settings.generated.resources.reset_dialog_cancel
import yatte.presentation.feature.settings.generated.resources.reset_dialog_confirm
import yatte.presentation.feature.settings.generated.resources.reset_dialog_message
import yatte.presentation.feature.settings.generated.resources.reset_dialog_title
import yatte.presentation.feature.settings.generated.resources.snackbar_export_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_export_success
import yatte.presentation.feature.settings.generated.resources.snackbar_import_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_import_success
import yatte.presentation.feature.settings.generated.resources.snackbar_reset_success
import yatte.presentation.feature.settings.generated.resources.snackbar_settings_saved
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_title
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

import com.segnities007.yatte.presentation.feature.settings.component.SettingsContent
import com.segnities007.yatte.presentation.feature.settings.component.SettingsDialogs
import com.segnities007.yatte.presentation.feature.settings.component.SettingsHeader
import com.segnities007.yatte.presentation.feature.settings.component.SettingsSideEffects

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    actions: SettingsActions,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isNavigationVisible: Boolean,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val fileHelper = rememberFileHelper()
    val soundPickerLauncher = rememberSoundPickerLauncher(
        onResult = { uri ->
            if (uri != null) {
                viewModel.onIntent(SettingsIntent.UpdateCustomSoundUri(uri))
            }
        },
    )

    SettingsHeader()
    SettingsDialogs(state = state, onIntent = viewModel::onIntent)
    SettingsSideEffects(
        viewModel = viewModel,
        actions = actions,
        fileHelper = fileHelper,
        onShowSnackbar = onShowSnackbar,
        scope = scope,
    )

    SettingsScreen(
        state = state,
        contentPadding = contentPadding,
        isNavigationVisible = isNavigationVisible,
        onIntent = viewModel::onIntent,
        onLicenseClick = actions.onLicenseClick,

        fileHelper = fileHelper,
        soundPickerLauncher = soundPickerLauncher,
        scope = scope,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    contentPadding: PaddingValues,
    isNavigationVisible: Boolean,
    onIntent: (SettingsIntent) -> Unit,
    onLicenseClick: () -> Unit,

    fileHelper: FileHelper,
    soundPickerLauncher: SoundPickerLauncher,
    scope: CoroutineScope,
    onShowSnackbar: (String) -> Unit,
) {
    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        SettingsContent(
            state = state,
            onIntent = onIntent,
            onLicenseClick = onLicenseClick,

            fileHelper = fileHelper,
            soundPickerLauncher = soundPickerLauncher,
            scope = scope,
            contentPadding = listContentPadding,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    YatteTheme {
        // Mock FileHelper and SoundPickerLauncher
        val fileHelper = rememberFileHelper()
        val soundPickerLauncher = rememberSoundPickerLauncher {}
        val scope = rememberCoroutineScope()

        SettingsScreen(
            state = SettingsState(),
            contentPadding = PaddingValues(0.dp),
            isNavigationVisible = true,
            onIntent = {},
            onLicenseClick = {},

            fileHelper = fileHelper,
            soundPickerLauncher = soundPickerLauncher,
            scope = scope,
            onShowSnackbar = {},
        )
    }
}


