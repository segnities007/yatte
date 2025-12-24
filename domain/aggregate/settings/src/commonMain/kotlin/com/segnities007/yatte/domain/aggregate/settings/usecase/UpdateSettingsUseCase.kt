package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository

/**
 * 設定更新ユースケース
 */
class UpdateSettingsUseCase(
    private val repository: SettingsRepository,
) {
    /**
     * 設定を更新する
     *
     * @param settings 新しい設定
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(settings: UserSettings): Result<Unit> =
        runCatching {
            repository.updateSettings(settings)
        }
}
