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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.VibrationPattern
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.file.FileHelper
import com.segnities007.yatte.presentation.core.file.rememberFileHelper
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.component.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.YatteConfirmDialog
import com.segnities007.yatte.presentation.designsystem.component.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.component.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.YatteSoundPicker
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.settings.component.SettingsActionRow
import com.segnities007.yatte.presentation.feature.settings.component.SettingsSliderRow
import com.segnities007.yatte.presentation.feature.settings.component.SettingsSwitchRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.nav_settings
import yatte.presentation.feature.settings.generated.resources.action_execute
import yatte.presentation.feature.settings.generated.resources.action_export_desc
import yatte.presentation.feature.settings.generated.resources.action_export_title
import yatte.presentation.feature.settings.generated.resources.action_import_desc
import yatte.presentation.feature.settings.generated.resources.action_import_title
import yatte.presentation.feature.settings.generated.resources.action_show
import yatte.presentation.feature.settings.generated.resources.custom_sound_clear
import yatte.presentation.feature.settings.generated.resources.custom_sound_default
import yatte.presentation.feature.settings.generated.resources.custom_sound_select
import yatte.presentation.feature.settings.generated.resources.custom_sound_selected
import yatte.presentation.feature.settings.generated.resources.custom_sound_title
import yatte.presentation.feature.settings.generated.resources.default_minutes_before
import yatte.presentation.feature.settings.generated.resources.notification_sound_desc
import yatte.presentation.feature.settings.generated.resources.notification_sound_title
import yatte.presentation.feature.settings.generated.resources.notification_vibration_desc
import yatte.presentation.feature.settings.generated.resources.notification_vibration_title
import yatte.presentation.feature.settings.generated.resources.reset_button
import yatte.presentation.feature.settings.generated.resources.reset_desc
import yatte.presentation.feature.settings.generated.resources.reset_dialog_cancel
import yatte.presentation.feature.settings.generated.resources.reset_dialog_confirm
import yatte.presentation.feature.settings.generated.resources.reset_dialog_message
import yatte.presentation.feature.settings.generated.resources.reset_dialog_title
import yatte.presentation.feature.settings.generated.resources.reset_title
import yatte.presentation.feature.settings.generated.resources.section_appearance
import yatte.presentation.feature.settings.generated.resources.section_data
import yatte.presentation.feature.settings.generated.resources.section_license_desc
import yatte.presentation.feature.settings.generated.resources.section_license_title
import yatte.presentation.feature.settings.generated.resources.section_notifications
import yatte.presentation.feature.settings.generated.resources.snackbar_export_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_export_success
import yatte.presentation.feature.settings.generated.resources.snackbar_import_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_import_success
import yatte.presentation.feature.settings.generated.resources.snackbar_reset_success
import yatte.presentation.feature.settings.generated.resources.snackbar_settings_saved
import yatte.presentation.feature.settings.generated.resources.snooze_duration_title
import yatte.presentation.feature.settings.generated.resources.theme_dark
import yatte.presentation.feature.settings.generated.resources.theme_light
import yatte.presentation.feature.settings.generated.resources.theme_system
import yatte.presentation.feature.settings.generated.resources.theme_title
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_heartbeat
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_long
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_normal
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_short
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_sos
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_title
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@OptIn(ExperimentalMaterial3Api::class)
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

    SettingsSetupHeader()
    SettingsSetupDialogs(state = state, onIntent = viewModel::onIntent)
    SettingsSetupSideEffects(
        viewModel = viewModel,
        actions = actions,
        fileHelper = fileHelper,
        onShowSnackbar = onShowSnackbar,
        scope = scope,
    )

    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        SettingsContent(
            state = state,
            onIntent = viewModel::onIntent,
            onLicenseClick = actions.onLicenseClick,
            fileHelper = fileHelper,
            soundPickerLauncher = soundPickerLauncher,
            scope = scope,
            contentPadding = listContentPadding,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

@Composable
private fun SettingsSetupHeader() {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val settingsTitle = stringResource(CoreRes.string.nav_settings)
    
    val headerConfig = remember(settingsTitle) {
        HeaderConfig(title = { Text(settingsTitle) })
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}

@Composable
private fun SettingsSetupDialogs(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
    if (state.showResetConfirmation) {
    if (state.showResetConfirmation) {
        YatteConfirmDialog(
            title = stringResource(SettingsRes.string.reset_dialog_title),
            message = stringResource(SettingsRes.string.reset_dialog_message),
            confirmText = stringResource(SettingsRes.string.reset_dialog_confirm),
            dismissText = stringResource(SettingsRes.string.reset_dialog_cancel),
            onConfirm = { onIntent(SettingsIntent.ConfirmResetData) },
            onDismiss = { onIntent(SettingsIntent.CancelResetData) },
            isDestructive = true,
        )
    }
    }
}

@Composable
private fun SettingsSetupSideEffects(
    viewModel: SettingsViewModel,
    actions: SettingsActions,
    fileHelper: FileHelper,
    onShowSnackbar: (String) -> Unit,
    scope: CoroutineScope,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> actions.onBack()
                is SettingsEvent.ShowError -> onShowSnackbar(event.message)
                is SettingsEvent.ShowSaveSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_settings_saved))
                is SettingsEvent.ShowResetSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_reset_success))
                is SettingsEvent.ShowExportReady -> {
                    fileHelper.saveFile(
                        fileName = "yatte_history_backup.json",
                        content = event.json,
                        onSuccess = {
                            scope.launch {
                                onShowSnackbar(getString(SettingsRes.string.snackbar_export_success))
                            }
                        },
                        onError = { e ->
                            scope.launch {
                                onShowSnackbar(getString(SettingsRes.string.snackbar_export_failed, e.message ?: "Unknown error"))
                            }
                        }
                    )
                }
                is SettingsEvent.ShowImportSuccess -> {
                    onShowSnackbar(getString(SettingsRes.string.snackbar_import_success, event.count))
                }
            }
        }
    }
}

@Composable
private fun SettingsContent(
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
private fun SettingsNotificationSection(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    soundPickerLauncher: SoundPickerLauncher,
) {
    YatteSectionCard(
        icon = Icons.Default.Notifications,
        title = stringResource(SettingsRes.string.section_notifications),
    ) {
        SettingsSwitchRow(
            title = stringResource(SettingsRes.string.notification_sound_title),
            subtitle = stringResource(SettingsRes.string.notification_sound_desc),
            checked = state.settings.notificationSound,
            onCheckedChange = { onIntent(SettingsIntent.ToggleNotificationSound(it)) },
        )

        SettingsSwitchRow(
            title = stringResource(SettingsRes.string.notification_vibration_title),
            subtitle = stringResource(SettingsRes.string.notification_vibration_desc),
            checked = state.settings.notificationVibration,
            onCheckedChange = { onIntent(SettingsIntent.ToggleNotificationVibration(it)) },
        )

        SettingsSliderRow(
            title = stringResource(
                SettingsRes.string.default_minutes_before,
                state.settings.defaultMinutesBefore,
            ),
            value = state.settings.defaultMinutesBefore,
            valueRange = 1f..60f,
            steps = 60,
            onValueChange = { onIntent(SettingsIntent.UpdateMinutesBefore(it)) },
        )

        SettingsSliderRow(
            title = stringResource(
                SettingsRes.string.snooze_duration_title,
                state.settings.snoozeDuration,
            ),
            value = state.settings.snoozeDuration,
            valueRange = 1f..60f,
            steps = 60,
            onValueChange = { onIntent(SettingsIntent.UpdateSnoozeDuration(it)) },
        )

        if (state.settings.notificationVibration) {
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            Text(
                text = stringResource(SettingsRes.string.vibration_pattern_title),
                style = MaterialTheme.typography.bodyMedium,
            )
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = YatteSpacing.xs),
            ) {
                VibrationPattern.entries.forEachIndexed { index, pattern ->
                    SegmentedButton(
                        selected = state.settings.vibrationPattern == pattern,
                        onClick = { onIntent(SettingsIntent.UpdateVibrationPattern(pattern)) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = VibrationPattern.entries.size,
                        ),
                    ) {
                        Text(
                            text = pattern.toUiLabel(),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                        )
                    }
                }
            }
        }

        if (state.settings.notificationSound) {
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            YatteSoundPicker(
                currentSoundUri = state.settings.customSoundUri,
                onSelectSound = { soundPickerLauncher.launch() },
                onClearSound = { onIntent(SettingsIntent.UpdateCustomSoundUri(null)) },
                title = stringResource(SettingsRes.string.custom_sound_title),
                selectedText = stringResource(SettingsRes.string.custom_sound_selected),
                defaultText = stringResource(SettingsRes.string.custom_sound_default),
                selectButtonText = stringResource(SettingsRes.string.custom_sound_select),
                clearContentDescription = stringResource(SettingsRes.string.custom_sound_clear),
            )
        }
    }
}

@Composable
private fun SettingsAppearanceSection(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
    YatteSectionCard(
        icon = Icons.Default.Palette,
        title = stringResource(SettingsRes.string.section_appearance),
    ) {
        Text(
            text = stringResource(SettingsRes.string.theme_title),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(YatteSpacing.sm))
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            ThemeMode.entries.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = state.settings.themeMode == mode,
                    onClick = { onIntent(SettingsIntent.UpdateThemeMode(mode)) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = ThemeMode.entries.size,
                    ),
                ) {
                    Text(mode.toUiLabel())
                }
            }
        }
    }
}

@Composable
private fun SettingsDataSection(
    onIntent: (SettingsIntent) -> Unit,
    onLicenseClick: () -> Unit,
    fileHelper: FileHelper,
    scope: CoroutineScope,
    onShowSnackbar: (String) -> Unit,
) {
    YatteSectionCard(
        icon = Icons.Default.Delete,
        title = stringResource(SettingsRes.string.section_data),
    ) {
        SettingsActionRow(
            title = stringResource(SettingsRes.string.section_license_title),
            subtitle = stringResource(SettingsRes.string.section_license_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.action_show),
                    onClick = onLicenseClick,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        )

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        SettingsActionRow(
            title = stringResource(SettingsRes.string.action_export_title),
            subtitle = stringResource(SettingsRes.string.action_export_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.action_execute),
                    onClick = { onIntent(SettingsIntent.RequestExportHistory) },
                )
            }
        )

        SettingsActionRow(
            title = stringResource(SettingsRes.string.action_import_title),
            subtitle = stringResource(SettingsRes.string.action_import_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.action_execute),
                    onClick = {
                        fileHelper.loadFile(
                            onLoaded = { json ->
                                onIntent(SettingsIntent.ImportHistory(json))
                            },
                            onError = { e ->
                                scope.launch {
                                    onShowSnackbar(getString(SettingsRes.string.snackbar_import_failed, e.message ?: "Unknown error"))
                                }
                            }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                )
            }
        )
        
        Spacer(modifier = Modifier.height(YatteSpacing.md))
        
        SettingsActionRow(
            title = stringResource(SettingsRes.string.reset_title),
            subtitle = stringResource(SettingsRes.string.reset_desc),
            action = {
                YatteButton(
                    text = stringResource(SettingsRes.string.reset_button),
                    onClick = { onIntent(SettingsIntent.RequestResetData) },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        )
    }
}

@Composable
private fun ThemeMode.toUiLabel(): String = when (this) {
    ThemeMode.LIGHT -> stringResource(SettingsRes.string.theme_light)
    ThemeMode.DARK -> stringResource(SettingsRes.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(SettingsRes.string.theme_system)
}

@Composable
private fun VibrationPattern.toUiLabel(): String = when (this) {
    VibrationPattern.NORMAL -> stringResource(SettingsRes.string.vibration_pattern_normal)
    VibrationPattern.SHORT -> stringResource(SettingsRes.string.vibration_pattern_short)
    VibrationPattern.LONG -> stringResource(SettingsRes.string.vibration_pattern_long)
    VibrationPattern.SOS -> stringResource(SettingsRes.string.vibration_pattern_sos)
    VibrationPattern.HEARTBEAT -> stringResource(SettingsRes.string.vibration_pattern_heartbeat)
}
