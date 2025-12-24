package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository

class ClearAllHistoryUseCase(
    private val repository: HistoryRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        runCatching {
            repository.deleteAll()
        }
}
