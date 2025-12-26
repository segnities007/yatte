package com.segnities007.yatte.presentation.feature.settings

import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings

/**
 * 設定画面の状態
 */
data class SettingsState(
    val settings: UserSettings = UserSettings(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showResetConfirmation: Boolean = false,
)

/**
 * 設定画面のインテント
 */
sealed interface SettingsIntent {
    data object LoadSettings : SettingsIntent
    data class UpdateMinutesBefore(val minutes: Int) : SettingsIntent
    data class ToggleNotificationSound(val enabled: Boolean) : SettingsIntent
    data class ToggleNotificationVibration(val enabled: Boolean) : SettingsIntent
    data class UpdateCustomSoundUri(val uri: String?) : SettingsIntent
    data class UpdateThemeMode(val mode: ThemeMode) : SettingsIntent
    data object NavigateBack : SettingsIntent
    data object RequestResetData : SettingsIntent
    data object ConfirmResetData : SettingsIntent
    data object CancelResetData : SettingsIntent
}

/**
 * 設定画面のイベント
 */
sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
    data class ShowError(val message: String) : SettingsEvent
    data object ShowSaveSuccess : SettingsEvent
    data object ShowResetSuccess : SettingsEvent
}
