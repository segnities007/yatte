package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * 履歴タイムライン取得ユースケース
 */
class GetHistoryTimelineUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * 指定した日付の履歴をFlowで取得
     *
     * @param date 取得する日付
     * @return 履歴のFlowリスト
     */
    operator fun invoke(date: LocalDate): Flow<List<History>> = repository.getByDate(date)

    /**
     * 全履歴を取得
     */
    fun getAll(): Flow<List<History>> = repository.getAll()
}
