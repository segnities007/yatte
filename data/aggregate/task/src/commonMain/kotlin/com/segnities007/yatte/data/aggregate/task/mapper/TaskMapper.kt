package com.segnities007.yatte.data.aggregate.task.mapper

import com.segnities007.yatte.data.core.database.entity.TaskEntity
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


/**
 * TaskEntity → Task (Domain Model)
 */
fun TaskEntity.toDomain(): Task {
    val timeZone = TimeZone.currentSystemDefault()
    val createdDateTime = Instant.fromEpochMilliseconds(createdAt).toLocalDateTime(timeZone)
    val alarmDateTime = Instant.fromEpochMilliseconds(alarmTime).toLocalDateTime(timeZone)

    return Task(
        id = TaskId(id),
        title = title,
        time = alarmDateTime.time,
        minutesBefore = minutesBefore,
        taskType = TaskType.valueOf(taskType),
        weekDays = parseWeekDays(weekDays),
        isCompleted = isCompleted,
        createdAt = createdDateTime,
        alarmTriggeredAt = alarmTriggeredAt?.let {
            Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone)
        },
        skipUntil = skipUntil?.let {
            LocalDate.fromEpochDays(it.toInt())
        },
    )
}

/**
 * Task (Domain Model) → TaskEntity
 */
fun Task.toEntity(): TaskEntity {
    val timeZone = TimeZone.currentSystemDefault()
    val alarmDateTime = LocalDateTime(createdAt.date, time)

    return TaskEntity(
        id = id.value,
        title = title,
        alarmTime = alarmDateTime.toInstant(timeZone).toEpochMilliseconds(),
        minutesBefore = minutesBefore,
        taskType = taskType.name,
        weekDays = weekDays.toJsonString(),
        isCompleted = isCompleted,
        createdAt = createdAt.toInstant(timeZone).toEpochMilliseconds(),
        alarmTriggeredAt = alarmTriggeredAt?.toInstant(timeZone)?.toEpochMilliseconds(),
        skipUntil = skipUntil?.toEpochDays()?.toLong(),
    )
}


private fun List<DayOfWeek>.toJsonString(): String =
    joinToString(",") { it.name }

private fun parseWeekDays(json: String): List<DayOfWeek> =
    if (json.isBlank()) emptyList()
    else json.split(",").map { DayOfWeek.valueOf(it.trim()) }
