package com.segnities007.yatte.data.aggregate.history.mapper

import com.segnities007.yatte.data.core.database.entity.HistoryEntity
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * HistoryEntity → History (Domain Model)
 *
 * 仕様:
 * - DBには日時を `epoch millis` で保存し、取り出すときは `TimeZone.currentSystemDefault()` で復元する。
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
 *
 * 仕様:
 * - DBには日時を `epoch millis` で保存する。
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
