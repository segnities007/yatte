package com.segnities007.yatte.data.aggregate.task.mapper

import com.segnities007.yatte.data.core.database.entity.TaskEntity
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import kotlinx.datetime.DayOfWeek
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


/**
 * TaskEntity → Task (Domain Model)
 *
 * 仕様:
 * - DBには時刻を `epoch millis (Long)` で保存し、取り出すときは `TimeZone.currentSystemDefault()` で復元する。
 * - `skipUntil` は日付のみを `epochDays` として保存する（タイムゾーンの影響を受けない）。
 * - `weekDays` はJSONではなく「カンマ区切りの列挙名」で保存する。
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
 *
 * 注意:
 * - `Task.time` は「壁時計の時刻（LocalTime）」のみのため、DBには任意の基準日と合成した `LocalDateTime` を
 *   `epoch millis` として格納する。
 * - 現在の実装では基準日として `createdAt.date` を用い、復元時は `.time` のみを利用する。
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


/**
 * 曜日一覧を「カンマ区切りの列挙名」として保存する。
 */
private fun List<DayOfWeek>.toJsonString(): String =
    joinToString(",") { it.name }

/**
 * 「カンマ区切りの列挙名」から曜日一覧を復元する。
 */
private fun parseWeekDays(json: String): List<DayOfWeek> =
    if (json.isBlank()) emptyList()
    else json.split(",").map { DayOfWeek.valueOf(it.trim()) }
