package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository

/**
 * 履歴削除ユースケース
 */
class DeleteHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * 履歴を削除する
     *
     * @param historyId 削除する履歴のID
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(historyId: HistoryId): Result<Unit> =
        runCatching {
            repository.delete(historyId)
        }
}
