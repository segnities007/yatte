package com.segnities007.yatte.presentation.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.presentation.core.component.FloatingHeaderBar
import com.segnities007.yatte.presentation.core.component.FloatingHeaderBarDefaults
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.nav_settings
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.settings.generated.resources.*
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes
import org.koin.compose.viewmodel.koinViewModel
import com.segnities007.yatte.presentation.feature.settings.component.SoundPicker
import com.segnities007.yatte.presentation.core.platform.FileType
import com.segnities007.yatte.presentation.core.platform.rememberFilePickerLauncher

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

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> actions.onBack()
                is SettingsEvent.ShowError -> onShowSnackbar(event.message)
                is SettingsEvent.ShowSaveSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_settings_saved))
                is SettingsEvent.ShowResetSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_reset_success))
            }
        }
    }

    // リセット確認ダイアログ
    if (state.showResetConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.onIntent(SettingsIntent.CancelResetData) },
            title = { Text(stringResource(SettingsRes.string.reset_dialog_title)) },
            text = { Text(stringResource(SettingsRes.string.reset_dialog_message)) },
            confirmButton = {
                Button(
                    onClick = { viewModel.onIntent(SettingsIntent.ConfirmResetData) },
                ) {
                    Text(stringResource(SettingsRes.string.reset_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(SettingsIntent.CancelResetData) },
                ) {
                    Text(stringResource(SettingsRes.string.reset_dialog_cancel))
                }
            },
        )
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val headerHeight = FloatingHeaderBarDefaults.ContainerHeight + FloatingHeaderBarDefaults.TopMargin + FloatingHeaderBarDefaults.BottomSpacing
    val headerPaddingValues = PaddingValues(top = statusBarHeight + headerHeight)
    
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = headerPaddingValues.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding())
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // 通知設定
            Text(
                text = stringResource(SettingsRes.string.section_notifications),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.notification_sound_title)) },
                supportingContent = { Text(stringResource(SettingsRes.string.notification_sound_desc)) },
                trailingContent = {
                    Switch(
                        checked = state.settings.notificationSound,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationSound(it)) },
                    )
                },
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.notification_vibration_title)) },
                supportingContent = { Text(stringResource(SettingsRes.string.notification_vibration_desc)) },
                trailingContent = {
                    Switch(
                        checked = state.settings.notificationVibration,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationVibration(it)) },
                    )
                },
            )

            ListItem(
                headlineContent = {
                    Text(
                        stringResource(
                            SettingsRes.string.default_minutes_before,
                            state.settings.defaultMinutesBefore,
                        )
                    )
                },
                supportingContent = {
                    Slider(
                        value = state.settings.defaultMinutesBefore.toFloat(),
                        onValueChange = { viewModel.onIntent(SettingsIntent.UpdateMinutesBefore(it.toInt())) },
                        valueRange = 0f..60f,
                        steps = 11,
                    )
                },
            )

            // ファイルピッカーランチャー
            val filePickerLauncher = rememberFilePickerLauncher(
                type = FileType.Audio,
                onResult = { uri ->
                    if (uri != null) {
                        viewModel.onIntent(SettingsIntent.UpdateCustomSoundUri(uri))
                    }
                },
            )

            // カスタム通知音（通知音がONの場合のみ表示）
            if (state.settings.notificationSound) {
                SoundPicker(
                    currentSoundUri = state.settings.customSoundUri,
                    onSelectSound = {
                        filePickerLauncher.launch()
                    },
                    onClearSound = {
                        viewModel.onIntent(SettingsIntent.UpdateCustomSoundUri(null))
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // テーマ設定
            Text(
                text = stringResource(SettingsRes.string.section_appearance),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.theme_title)) },
                supportingContent = {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        ThemeMode.entries.forEachIndexed { index, mode ->
                            SegmentedButton(
                                selected = state.settings.themeMode == mode,
                                onClick = { viewModel.onIntent(SettingsIntent.UpdateThemeMode(mode)) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = ThemeMode.entries.size,
                                ),
                            ) {
                                Text(mode.toDisplayString())
                            }
                        }
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // データ管理
            Text(
                text = stringResource(SettingsRes.string.section_data),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.reset_title)) },
                supportingContent = { Text(stringResource(SettingsRes.string.reset_desc)) },
                trailingContent = {
                    OutlinedButton(
                        onClick = { viewModel.onIntent(SettingsIntent.RequestResetData) },
                    ) {
                        Text(stringResource(SettingsRes.string.reset_button))
                    }
                },
            )
        }

        FloatingHeaderBar(
            title = { Text(stringResource(CoreRes.string.nav_settings)) },
            isVisible = isNavigationVisible,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ThemeMode.toDisplayString(): String = when (this) {
    ThemeMode.LIGHT -> stringResource(SettingsRes.string.theme_light)
    ThemeMode.DARK -> stringResource(SettingsRes.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(SettingsRes.string.theme_system)
}
