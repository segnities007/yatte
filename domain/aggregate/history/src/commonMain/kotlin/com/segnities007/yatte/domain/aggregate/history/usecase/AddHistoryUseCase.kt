package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository

/**
 * 履歴追加ユースケース
 */
class AddHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * 履歴を追加する
     *
     * @param history 追加する履歴
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(history: History): Result<Unit> =
        runCatching {
            repository.insert(history)
        }
}
