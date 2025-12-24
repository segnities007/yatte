package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.settings.model.UserSettings
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
class GetSettingsUseCase(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<UserSettings> = repository.getSettings()
}
