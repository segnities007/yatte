package com.segnities007.yatte.presentation.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.usecase.GetSettingsUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 設定画面のViewModel
 */
class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadSettings()
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.LoadSettings -> loadSettings()
            is SettingsIntent.UpdateMinutesBefore -> updateMinutesBefore(intent.minutes)
            is SettingsIntent.ToggleNotificationSound -> toggleNotificationSound(intent.enabled)
            is SettingsIntent.ToggleNotificationVibration -> toggleNotificationVibration(intent.enabled)
            is SettingsIntent.UpdateThemeMode -> updateThemeMode(intent.mode)
            is SettingsIntent.NavigateBack -> sendEvent(SettingsEvent.NavigateBack)
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getSettingsUseCase().collect { settings ->
                _state.update { it.copy(settings = settings, isLoading = false) }
            }
        }
    }

    private fun updateMinutesBefore(minutes: Int) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(defaultMinutesBefore = minutes)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private fun toggleNotificationSound(enabled: Boolean) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(notificationSound = enabled)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private fun toggleNotificationVibration(enabled: Boolean) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(notificationVibration = enabled)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(themeMode = mode)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private suspend fun saveSettings(settings: UserSettings) {
        updateSettingsUseCase(settings)
            .onFailure { error ->
                sendEvent(SettingsEvent.ShowError(error.message ?: "設定の保存に失敗しました"))
            }
    }

    private fun sendEvent(event: SettingsEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
}
