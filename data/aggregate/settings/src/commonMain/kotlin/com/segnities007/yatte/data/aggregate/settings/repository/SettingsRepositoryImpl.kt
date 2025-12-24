package com.segnities007.yatte.data.aggregate.settings.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SettingsRepository の DataStore 実装
 */
class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private companion object {
        val KEY_DEFAULT_MINUTES_BEFORE = intPreferencesKey("default_minutes_before")
        val KEY_NOTIFICATION_SOUND = booleanPreferencesKey("notification_sound")
        val KEY_NOTIFICATION_VIBRATION = booleanPreferencesKey("notification_vibration")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
    }

    override fun getSettings(): Flow<UserSettings> =
        dataStore.data.map { preferences ->
            UserSettings(
                defaultMinutesBefore = preferences[KEY_DEFAULT_MINUTES_BEFORE] ?: 10,
                notificationSound = preferences[KEY_NOTIFICATION_SOUND] ?: true,
                notificationVibration = preferences[KEY_NOTIFICATION_VIBRATION] ?: true,
                themeMode = preferences[KEY_THEME_MODE]?.let { ThemeMode.valueOf(it) }
                    ?: ThemeMode.SYSTEM,
            )
        }

    override suspend fun updateSettings(settings: UserSettings) {
        dataStore.edit { preferences ->
            preferences[KEY_DEFAULT_MINUTES_BEFORE] = settings.defaultMinutesBefore
            preferences[KEY_NOTIFICATION_SOUND] = settings.notificationSound
            preferences[KEY_NOTIFICATION_VIBRATION] = settings.notificationVibration
            preferences[KEY_THEME_MODE] = settings.themeMode.name
        }
    }
}
