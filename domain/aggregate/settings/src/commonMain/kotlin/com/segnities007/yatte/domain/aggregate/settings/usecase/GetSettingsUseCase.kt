package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

/**
 * 設定取得ユースケース
 */
class GetSettingsUseCase(
    private val repository: SettingsRepository,
) {
    /**
     * 現在の設定をFlowで取得
     */
    operator fun invoke(): Flow<UserSettings> = repository.getSettings()
}
