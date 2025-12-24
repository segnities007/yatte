package com.segnities007.yatte.data.aggregate.alarm.mapper

import com.segnities007.yatte.data.core.database.entity.AlarmEntity
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * AlarmEntity → Alarm (Domain Model)
 */
fun AlarmEntity.toDomain(): Alarm {
    val timeZone = TimeZone.currentSystemDefault()
    return Alarm(
        id = AlarmId(id),
        taskId = TaskId(taskId),
        scheduledAt = Instant.fromEpochMilliseconds(scheduledAt).toLocalDateTime(timeZone),
        notifyAt = Instant.fromEpochMilliseconds(notifyAt).toLocalDateTime(timeZone),
        isTriggered = isTriggered,
    )
}

/**
 * Alarm (Domain Model) → AlarmEntity
 */
fun Alarm.toEntity(): AlarmEntity {
    val timeZone = TimeZone.currentSystemDefault()
    return AlarmEntity(
        id = id.value,
        taskId = taskId.value,
        scheduledAt = scheduledAt.toInstant(timeZone).toEpochMilliseconds(),
        notifyAt = notifyAt.toInstant(timeZone).toEpochMilliseconds(),
        isTriggered = isTriggered,
    )
}
