package com.segnities007.yatte.data.aggregate.alarm.repository

import com.segnities007.yatte.data.aggregate.alarm.mapper.toDomain
import com.segnities007.yatte.data.aggregate.alarm.mapper.toEntity
import com.segnities007.yatte.data.core.database.dao.AlarmDao
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * AlarmRepository の実装
 */
class AlarmRepositoryImpl(
    private val dao: AlarmDao,
) : AlarmRepository {

    override suspend fun schedule(alarm: Alarm) =
        dao.insert(alarm.toEntity())

    override suspend fun cancel(alarmId: AlarmId) =
        dao.deleteById(alarmId.value)

    override suspend fun cancelByTaskId(taskId: TaskId) =
        dao.deleteByTaskId(taskId.value)

    override fun getScheduledAlarms(): Flow<List<Alarm>> =
        dao.getScheduledAlarms().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun markTriggered(alarmId: AlarmId) =
        dao.markTriggered(alarmId.value)
}
