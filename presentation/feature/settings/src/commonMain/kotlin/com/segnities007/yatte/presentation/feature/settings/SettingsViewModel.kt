package com.segnities007.yatte.presentation.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeColor
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.model.VibrationPattern
import com.segnities007.yatte.domain.aggregate.settings.usecase.ExportUserDataUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.GetSettingsUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.ImportUserDataUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.ResetAllDataUseCase
import com.segnities007.yatte.domain.aggregate.settings.usecase.UpdateSettingsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import yatte.presentation.feature.settings.generated.resources.error_settings_save_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_export_failed
import yatte.presentation.feature.settings.generated.resources.snackbar_import_failed
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val resetAllDataUseCase: ResetAllDataUseCase,
    private val exportUserDataUseCase: ExportUserDataUseCase,
    private val importUserDataUseCase: ImportUserDataUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var loadSettingsJob: Job? = null

    init {
        loadSettings()
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.LoadSettings -> loadSettings()
            is SettingsIntent.UpdateMinutesBefore -> updateMinutesBefore(intent.minutes)
            is SettingsIntent.ToggleNotificationSound -> toggleNotificationSound(intent.enabled)
            is SettingsIntent.ToggleNotificationVibration -> toggleNotificationVibration(intent.enabled)
            is SettingsIntent.UpdateCustomSoundUri -> updateCustomSoundUri(intent.uri)
            is SettingsIntent.UpdateSnoozeDuration -> updateSnoozeDuration(intent.minutes)
            is SettingsIntent.UpdateVibrationPattern -> updateVibrationPattern(intent.pattern)
            is SettingsIntent.UpdateThemeMode -> updateThemeMode(intent.mode)
            is SettingsIntent.UpdateThemeColor -> updateThemeColor(intent.color)
            is SettingsIntent.NavigateBack -> sendEvent(SettingsEvent.NavigateBack)
            is SettingsIntent.RequestResetData -> showResetConfirmation()
            is SettingsIntent.ConfirmResetData -> confirmResetData()
            is SettingsIntent.CancelResetData -> hideResetConfirmation()
            is SettingsIntent.RequestExportHistory -> exportHistory()
            is SettingsIntent.ImportHistory -> importHistory(intent.json)
        }
    }

    private fun exportHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            exportUserDataUseCase.invoke()
                .onSuccess { json ->
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(SettingsEvent.ShowExportReady(json))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    val errorMessage = error.message ?: "Unknown error"
                    val message = getString(SettingsRes.string.snackbar_export_failed, errorMessage)
                    sendEvent(SettingsEvent.ShowError(message))
                }
        }
    }

    private fun importHistory(json: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            importUserDataUseCase.invoke(json)
                .onSuccess { count ->
                    _state.update { it.copy(isLoading = false) }
                    sendEvent(SettingsEvent.ShowImportSuccess(count))
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
                    val errorMessage = error.message ?: "Unknown error"
                    val message = getString(SettingsRes.string.snackbar_import_failed, errorMessage)
                    sendEvent(SettingsEvent.ShowError(message))
                }
        }
    }

    private fun loadSettings() {
        loadSettingsJob?.cancel()
        loadSettingsJob = viewModelScope.launch {
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

    private fun updateThemeColor(color: ThemeColor) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(themeColor = color)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private fun updateCustomSoundUri(uri: String?) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(customSoundUri = uri)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private fun updateSnoozeDuration(minutes: Int) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(snoozeDuration = minutes)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private fun updateVibrationPattern(pattern: VibrationPattern) {
        viewModelScope.launch {
            val newSettings = _state.value.settings.copy(vibrationPattern = pattern)
            _state.update { it.copy(settings = newSettings) }
            saveSettings(newSettings)
        }
    }

    private suspend fun saveSettings(settings: UserSettings) {
        updateSettingsUseCase(settings)
            .onFailure { error ->
                val message = error.message ?: getString(SettingsRes.string.error_settings_save_failed)
                sendEvent(SettingsEvent.ShowError(message))
            }
    }

    private fun sendEvent(event: SettingsEvent) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    private fun showResetConfirmation() {
        _state.update { it.copy(showResetConfirmation = true) }
    }

    private fun hideResetConfirmation() {
        _state.update { it.copy(showResetConfirmation = false) }
    }

    private fun confirmResetData() {
        viewModelScope.launch {
            hideResetConfirmation()
            resetAllDataUseCase()
                .onSuccess {
                    sendEvent(SettingsEvent.ShowResetSuccess)
                }
                .onFailure { error ->
                    val message = error.message ?: getString(SettingsRes.string.error_settings_save_failed)
                    sendEvent(SettingsEvent.ShowError(message))
                }
        }
    }
}
