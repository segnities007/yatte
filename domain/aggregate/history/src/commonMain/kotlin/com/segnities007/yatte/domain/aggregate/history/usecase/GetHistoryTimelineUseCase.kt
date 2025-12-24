package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
class GetHistoryTimelineUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * [date] の履歴をFlowで取得する。
     */
    operator fun invoke(date: LocalDate): Flow<List<History>> = repository.getByDate(date)

    fun getAll(): Flow<List<History>> = repository.getAll()
}
