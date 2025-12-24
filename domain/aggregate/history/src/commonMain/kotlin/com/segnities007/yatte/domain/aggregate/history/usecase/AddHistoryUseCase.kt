package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository

class AddHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * [history] を追加する。
     */
    suspend operator fun invoke(history: History): Result<Unit> =
        runCatching {
            repository.insert(history)
        }
}
