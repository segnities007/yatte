package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.segnities007.yatte.presentation.core.file.FileHelper
import com.segnities007.yatte.presentation.feature.settings.SettingsActions
import com.segnities007.yatte.presentation.feature.settings.SettingsEvent
import com.segnities007.yatte.presentation.feature.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import yatte.presentation.feature.settings.generated.resources.snackbar_export_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_export_success
import yatte.presentation.feature.settings.generated.resources.snackbar_import_success
import yatte.presentation.feature.settings.generated.resources.snackbar_reset_success
import yatte.presentation.feature.settings.generated.resources.snackbar_settings_saved
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@Composable
fun SettingsSideEffects(
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
