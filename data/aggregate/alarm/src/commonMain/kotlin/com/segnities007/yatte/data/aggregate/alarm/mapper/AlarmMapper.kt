package com.segnities007.yatte.data.aggregate.alarm.mapper

import com.segnities007.yatte.data.core.database.entity.AlarmEntity
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * AlarmEntity → Alarm (Domain Model)
 *
 * 仕様:
 * - DBには日時を `epoch millis` で保存し、取り出すときは `TimeZone.currentSystemDefault()` で復元する。
 */
fun AlarmEntity.toDomain(): Alarm {
    val timeZone = TimeZone.currentSystemDefault()
    return Alarm(
        id = AlarmId(id),
        taskId = TaskId(taskId),
        scheduledAt = Instant.fromEpochMilliseconds(scheduledAt).toLocalDateTime(timeZone),
        notifyAt = Instant.fromEpochMilliseconds(notifyAt).toLocalDateTime(timeZone),
        isTriggered = isTriggered,
        soundUri = soundUri,
    )
}

/**
 * Alarm (Domain Model) → AlarmEntity
 *
 * 仕様:
 * - DBには日時を `epoch millis` で保存する。
 */
fun Alarm.toEntity(): AlarmEntity {
    val timeZone = TimeZone.currentSystemDefault()
    return AlarmEntity(
        id = id.value,
        taskId = taskId.value,
        scheduledAt = scheduledAt.toInstant(timeZone).toEpochMilliseconds(),
        notifyAt = notifyAt.toInstant(timeZone).toEpochMilliseconds(),
        isTriggered = isTriggered,
        soundUri = soundUri,
    )
}
