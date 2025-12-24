package com.segnities007.yatte.domain.aggregate.settings.repository

import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * 設定リポジトリのインターフェース
 */
interface SettingsRepository {
    /**
     * 現在の設定を取得
     */
    fun getSettings(): Flow<UserSettings>

    /**
     * 設定を更新
     */
    suspend fun updateSettings(settings: UserSettings)
}
