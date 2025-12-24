package com.segnities007.yatte.data.aggregate.history.repository

import com.segnities007.yatte.data.aggregate.history.mapper.toDomain
import com.segnities007.yatte.data.aggregate.history.mapper.toEntity
import com.segnities007.yatte.data.core.database.dao.HistoryDao
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus

/**
 * HistoryRepository の実装
 */
class HistoryRepositoryImpl(
    private val dao: HistoryDao,
) : HistoryRepository {

    override fun getAll(): Flow<List<History>> =
        dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getByDate(date: LocalDate): Flow<List<History>> {
        val timeZone = TimeZone.currentSystemDefault()
        val startOfDay = date.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val nextDay = date.plus(1, DateTimeUnit.DAY)
        val endOfDay = nextDay.atStartOfDayIn(timeZone).toEpochMilliseconds()

        return dao.getByDate(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }


    override suspend fun insert(history: History) =
        dao.insert(history.toEntity())

    override suspend fun delete(id: HistoryId) =
        dao.deleteById(id.value)

    override suspend fun deleteAll() =
        dao.deleteAll()
}
