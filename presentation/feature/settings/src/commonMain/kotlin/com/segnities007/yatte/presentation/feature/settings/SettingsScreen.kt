package com.segnities007.yatte.presentation.feature.settings

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import com.segnities007.yatte.presentation.feature.settings.component.SettingsSectionCard
import com.segnities007.yatte.presentation.feature.settings.component.SettingsSliderRow
import com.segnities007.yatte.presentation.feature.settings.component.SettingsSwitchRow
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
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
    val topPadding = statusBarHeight + headerHeight
    val bottomPadding = contentPadding.calculateBottomPadding()

    // ファイルピッカーランチャー
    val filePickerLauncher = rememberFilePickerLauncher(
        type = FileType.Audio,
        onResult = { uri ->
            if (uri != null) {
                viewModel.onIntent(SettingsIntent.UpdateCustomSoundUri(uri))
            }
        },
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // トップのスペーサー（ヘッダー分）
            Spacer(modifier = Modifier.height(topPadding))
            SettingsSectionCard(
                icon = Icons.Default.Notifications,
                title = stringResource(SettingsRes.string.section_notifications),
            ) {
                SettingsSwitchRow(
                    title = stringResource(SettingsRes.string.notification_sound_title),
                    subtitle = stringResource(SettingsRes.string.notification_sound_desc),
                    checked = state.settings.notificationSound,
                    onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationSound(it)) },
                )

                SettingsSwitchRow(
                    title = stringResource(SettingsRes.string.notification_vibration_title),
                    subtitle = stringResource(SettingsRes.string.notification_vibration_desc),
                    checked = state.settings.notificationVibration,
                    onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationVibration(it)) },
                )

                SettingsSliderRow(
                    title = stringResource(
                        SettingsRes.string.default_minutes_before,
                        state.settings.defaultMinutesBefore,
                    ),
                    value = state.settings.defaultMinutesBefore,
                    onValueChange = { viewModel.onIntent(SettingsIntent.UpdateMinutesBefore(it)) },
                )

                // カスタム通知音（通知音がONの場合のみ表示）
                if (state.settings.notificationSound) {
                    Spacer(modifier = Modifier.height(8.dp))
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
            }

            // 外観設定セクション
            SettingsSectionCard(
                icon = Icons.Default.Palette,
                title = stringResource(SettingsRes.string.section_appearance),
            ) {
                Text(
                    text = stringResource(SettingsRes.string.theme_title),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(12.dp))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth(),
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
            }

            // データ管理セクション
            SettingsSectionCard(
                icon = Icons.Default.Delete,
                title = stringResource(SettingsRes.string.section_data),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(SettingsRes.string.reset_title),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = stringResource(SettingsRes.string.reset_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Button(
                        onClick = { viewModel.onIntent(SettingsIntent.RequestResetData) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(stringResource(SettingsRes.string.reset_button))
                    }
                }
            }

            // 下部の余白（ボトムナビゲーション分）
            Spacer(modifier = Modifier.height(bottomPadding))
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
