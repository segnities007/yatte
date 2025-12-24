package com.segnities007.yatte.domain.aggregate.history.repository

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
interface HistoryRepository {
    fun getByDate(date: LocalDate): Flow<List<History>>

    fun getAll(): Flow<List<History>>

    suspend fun insert(history: History)

    suspend fun delete(id: HistoryId)

    suspend fun deleteAll()
}
