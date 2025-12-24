package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository

/**
 * 履歴全削除ユースケース
 */
class ClearAllHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * すべての履歴を削除する
     *
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(): Result<Unit> =
        runCatching {
            repository.deleteAll()
        }
}
