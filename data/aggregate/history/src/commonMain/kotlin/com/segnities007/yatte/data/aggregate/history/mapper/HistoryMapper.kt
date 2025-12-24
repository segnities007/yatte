package com.segnities007.yatte.data.aggregate.history.mapper

import com.segnities007.yatte.data.core.database.entity.HistoryEntity
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * HistoryEntity → History (Domain Model)
 */
fun HistoryEntity.toDomain(): History {
    val timeZone = TimeZone.currentSystemDefault()
    return History(
        id = HistoryId(id),
        taskId = TaskId(taskId),
        title = title,
        completedAt = Instant.fromEpochMilliseconds(completedAt).toLocalDateTime(timeZone),
    )
}

/**
 * History (Domain Model) → HistoryEntity
 */
fun History.toEntity(): HistoryEntity {
    val timeZone = TimeZone.currentSystemDefault()
    return HistoryEntity(
        id = id.value,
        taskId = taskId.value,
        title = title,
        completedAt = completedAt.toInstant(timeZone).toEpochMilliseconds(),
    )
}
