package com.segnities007.yatte.data.aggregate.task.repository

import com.segnities007.yatte.data.aggregate.task.mapper.toDomain
import com.segnities007.yatte.data.aggregate.task.mapper.toEntity
import com.segnities007.yatte.data.core.database.dao.TaskDao
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * TaskRepository の実装
 */
class TaskRepositoryImpl(
    private val dao: TaskDao,
) : TaskRepository {

    override fun getTodayTasks(): Flow<List<Task>> =
        dao.getAll().map { entities ->
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            entities.map { it.toDomain() }
                .filter { it.isActiveOn(today) }
        }

    override fun getAllTasks(): Flow<List<Task>> =
        dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getById(id: TaskId): Task? =
        dao.getById(id.value)?.toDomain()

    override suspend fun insert(task: Task) =
        dao.insert(task.toEntity())

    override suspend fun update(task: Task) =
        dao.update(task.toEntity())

    override suspend fun delete(id: TaskId) =
        dao.deleteById(id.value)

    override suspend fun deleteExpiredTasks() {
        val now = Clock.System.now()
        val thresholdMillis = now.toEpochMilliseconds() - (24 * 60 * 60 * 1000)
        dao.deleteExpired(thresholdMillis)
    }
}
