package com.segnities007.yatte.domain.aggregate.settings.repository

import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import kotlinx.coroutines.flow.Flow
interface SettingsRepository {
    fun getSettings(): Flow<UserSettings>

    suspend fun updateSettings(settings: UserSettings)
}
