package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteConfirmDialog
import com.segnities007.yatte.presentation.feature.settings.SettingsIntent
import com.segnities007.yatte.presentation.feature.settings.SettingsState
import yatte.presentation.feature.settings.generated.resources.reset_dialog_cancel
import yatte.presentation.feature.settings.generated.resources.reset_dialog_confirm
import yatte.presentation.feature.settings.generated.resources.reset_dialog_message
import yatte.presentation.feature.settings.generated.resources.reset_dialog_title
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@Composable
fun SettingsDialogs(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
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
