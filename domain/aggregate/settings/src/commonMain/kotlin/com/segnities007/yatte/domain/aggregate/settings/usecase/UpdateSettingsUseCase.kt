package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository,
) {
    /**
     * [settings] で設定を更新する。
     */
    suspend operator fun invoke(settings: UserSettings): Result<Unit> =
        runCatching {
            repository.updateSettings(settings)
        }
}
