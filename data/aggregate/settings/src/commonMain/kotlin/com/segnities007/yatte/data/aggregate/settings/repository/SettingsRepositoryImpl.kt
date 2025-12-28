package com.segnities007.yatte.data.aggregate.settings.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.model.VibrationPattern
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SettingsRepository の DataStore 実装。
 *
 * 注意:
 * - `theme_mode` は文字列として保存するため、将来値が増えたり壊れた値が混入してもクラッシュしないよう
 *   `ThemeMode.SYSTEM` にフォールバックする。
 */
class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private companion object {
        val KEY_DEFAULT_MINUTES_BEFORE = intPreferencesKey("default_minutes_before")
        val KEY_NOTIFICATION_SOUND = booleanPreferencesKey("notification_sound")
        val KEY_NOTIFICATION_VIBRATION = booleanPreferencesKey("notification_vibration")
        val KEY_CUSTOM_SOUND_URI = stringPreferencesKey("custom_sound_uri")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_SNOOZE_DURATION = intPreferencesKey("snooze_duration")
        val KEY_VIBRATION_PATTERN = stringPreferencesKey("vibration_pattern")
    }

    override fun getSettings(): Flow<UserSettings> =
        dataStore.data.map { preferences ->
            val themeModeRaw = preferences[KEY_THEME_MODE]
            UserSettings(
                defaultMinutesBefore = preferences[KEY_DEFAULT_MINUTES_BEFORE] ?: 10,
                notificationSound = preferences[KEY_NOTIFICATION_SOUND] ?: true,
                notificationVibration = preferences[KEY_NOTIFICATION_VIBRATION] ?: true,
                customSoundUri = preferences[KEY_CUSTOM_SOUND_URI],
                themeMode = themeModeRaw
                    ?.let { raw -> ThemeMode.entries.firstOrNull { it.name == raw } }
                    ?: ThemeMode.SYSTEM,
                snoozeDuration = preferences[KEY_SNOOZE_DURATION] ?: 10,
                vibrationPattern = preferences[KEY_VIBRATION_PATTERN]
                    ?.let { raw -> VibrationPattern.entries.firstOrNull { it.name == raw } }
                    ?: VibrationPattern.NORMAL,
            )
        }

    override suspend fun updateSettings(settings: UserSettings) {
        dataStore.edit { preferences ->
            preferences[KEY_DEFAULT_MINUTES_BEFORE] = settings.defaultMinutesBefore
            preferences[KEY_NOTIFICATION_SOUND] = settings.notificationSound
            preferences[KEY_NOTIFICATION_VIBRATION] = settings.notificationVibration
            val soundUri = settings.customSoundUri
            if (soundUri != null) {
                preferences[KEY_CUSTOM_SOUND_URI] = soundUri
            } else {
                preferences.remove(KEY_CUSTOM_SOUND_URI)
            }
            preferences[KEY_THEME_MODE] = settings.themeMode.name
            preferences[KEY_SNOOZE_DURATION] = settings.snoozeDuration
            preferences[KEY_VIBRATION_PATTERN] = settings.vibrationPattern.name
        }
    }
}
