package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository

class DeleteHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * [historyId] の履歴を削除する。
     */
    suspend operator fun invoke(historyId: HistoryId): Result<Unit> =
        runCatching {
            repository.delete(historyId)
        }
}
