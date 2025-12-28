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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.VibrationPattern
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.component.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
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
import com.segnities007.yatte.presentation.feature.settings.component.SettingsActionRow
import com.segnities007.yatte.presentation.core.component.SoundPicker
import com.segnities007.yatte.presentation.core.file.rememberFileHelper
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher

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
                    modifier = Modifier.bounceClick(),
                ) {
                    Text(stringResource(SettingsRes.string.reset_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIntent(SettingsIntent.CancelResetData) },
                    modifier = Modifier.bounceClick(),
                ) {
                    Text(stringResource(SettingsRes.string.reset_dialog_cancel))
                }
            },
        )
    }

    // サウンドピッカーランチャー
    val soundPickerLauncher = rememberSoundPickerLauncher(
        onResult = { uri ->
            if (uri != null) {
                viewModel.onIntent(SettingsIntent.UpdateCustomSoundUri(uri))
            }
        },
    )
    
    val fileHelper = rememberFileHelper()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> actions.onBack()
                is SettingsEvent.ShowError -> onShowSnackbar(event.message)
                is SettingsEvent.ShowSaveSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_settings_saved))
                is SettingsEvent.ShowResetSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_reset_success))
                is SettingsEvent.ShowExportReady -> {
                    // JSONを受け取ったらファイル保存フローを開始
                    fileHelper.saveFile(
                        fileName = "yatte_history_backup.json",
                        content = event.json,
                        onSuccess = {
                            onShowSnackbar("エクスポート完了") // TODO: Resource
                        },
                        onError = { e ->
                            onShowSnackbar("エクスポート失敗: ${e.message}")
                        }
                    )
                }
                is SettingsEvent.ShowImportSuccess -> {
                    onShowSnackbar("${event.count}件の履歴をインポートしました") // TODO: Resource
                }
            }
        }
    }

    // グローバルHeaderの設定（SideEffectで即座に更新）
    val setHeaderConfig = LocalSetHeaderConfig.current
    val settingsTitle = stringResource(CoreRes.string.nav_settings)
    
    val headerConfig = remember {
        HeaderConfig(
            title = { Text(settingsTitle) },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }

    // YatteScaffold を使用（Headerはグローバルなので省略）
    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = listContentPadding.calculateTopPadding(),
                    bottom = listContentPadding.calculateBottomPadding(),
                    start = YatteSpacing.md,
                    end = YatteSpacing.md,
                ),
            verticalArrangement = Arrangement.spacedBy(YatteSpacing.md),
        ) {
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
                    valueRange = 0f..60f,
                    steps = 60,
                    onValueChange = { viewModel.onIntent(SettingsIntent.UpdateMinutesBefore(it)) },
                )

                SettingsSliderRow(
                    title = stringResource(
                        SettingsRes.string.snooze_duration_title,
                        state.settings.snoozeDuration,
                    ),
                    value = state.settings.snoozeDuration,
                    valueRange = 0f..60f,
                    steps = 60,
                    onValueChange = { viewModel.onIntent(SettingsIntent.UpdateSnoozeDuration(it)) },
                )

                // 振動パターン（振動がONの場合のみ表示）
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
                        // 5つは多いのでスクロール可能にするか、主要な3つにするか...
                        // ここではとりあえず全部表示し、文字サイズを調整するか、スクロールコンテナに入れる
                        // SegmentedButtonはスクロール非対応なので、RowでWrapする
                        VibrationPattern.entries.forEachIndexed { index, pattern ->
                            SegmentedButton(
                                selected = state.settings.vibrationPattern == pattern,
                                onClick = { viewModel.onIntent(SettingsIntent.UpdateVibrationPattern(pattern)) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = VibrationPattern.entries.size,
                                ),
                            ) {
                                Text(
                                    text = pattern.toDisplayString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }

                // カスタム通知音（通知音がONの場合のみ表示）
                if (state.settings.notificationSound) {
                    Spacer(modifier = Modifier.height(YatteSpacing.xs))
                    SoundPicker(
                        currentSoundUri = state.settings.customSoundUri,
                        onSelectSound = {
                            soundPickerLauncher.launch()
                        },
                        onClearSound = {
                            viewModel.onIntent(SettingsIntent.UpdateCustomSoundUri(null))
                        },
                        title = stringResource(SettingsRes.string.custom_sound_title),
                        selectedText = stringResource(SettingsRes.string.custom_sound_selected),
                        defaultText = stringResource(SettingsRes.string.custom_sound_default),
                        selectButtonText = stringResource(SettingsRes.string.custom_sound_select),
                        clearContentDescription = stringResource(SettingsRes.string.custom_sound_clear),
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
                Spacer(modifier = Modifier.height(YatteSpacing.sm))
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
                // エクスポート・インポート
                
                // エクスポート
                SettingsActionRow(
                    title = "エクスポート", // TODO: Resource
                    subtitle = "データをファイルに保存", // TODO: Resource
                    action = {
                        Button(
                            onClick = { viewModel.onIntent(SettingsIntent.RequestExportHistory) },
                            modifier = Modifier.bounceClick(),
                        ) {
                            Text("実行") // TODO: Resource
                        }
                    }
                )

                // インポート
                SettingsActionRow(
                    title = "インポート", // TODO: Resource
                    subtitle = "ファイルから復元（追記）", // TODO: Resource
                    action = {
                        Button(
                            onClick = {
                                fileHelper.loadFile(
                                    onLoaded = { json ->
                                        viewModel.onIntent(SettingsIntent.ImportHistory(json))
                                    },
                                    onError = { e ->
                                        onShowSnackbar("インポートに失敗しました: ${e.message}")
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                            ),
                            modifier = Modifier.bounceClick(),
                        ) {
                            Text("実行") // TODO: Resource
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(YatteSpacing.md))
                
                // リセット
                SettingsActionRow(
                    title = stringResource(SettingsRes.string.reset_title),
                    subtitle = stringResource(SettingsRes.string.reset_desc),
                    action = {
                        Button(
                            onClick = { viewModel.onIntent(SettingsIntent.RequestResetData) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.bounceClick(),
                        ) {
                            Text(stringResource(SettingsRes.string.reset_button))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeMode.toDisplayString(): String = when (this) {
    ThemeMode.LIGHT -> stringResource(SettingsRes.string.theme_light)
    ThemeMode.DARK -> stringResource(SettingsRes.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(SettingsRes.string.theme_system)
}

@Composable
private fun VibrationPattern.toDisplayString(): String = when (this) {
    VibrationPattern.NORMAL -> stringResource(SettingsRes.string.vibration_pattern_normal)
    VibrationPattern.SHORT -> stringResource(SettingsRes.string.vibration_pattern_short)
    VibrationPattern.LONG -> stringResource(SettingsRes.string.vibration_pattern_long)
    VibrationPattern.SOS -> stringResource(SettingsRes.string.vibration_pattern_sos)
    VibrationPattern.HEARTBEAT -> stringResource(SettingsRes.string.vibration_pattern_heartbeat)
}
