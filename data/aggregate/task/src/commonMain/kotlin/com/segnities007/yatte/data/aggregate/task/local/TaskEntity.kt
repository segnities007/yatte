package com.segnities007.yatte.data.aggregate.task.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * タスクのDBエンティティ
 */
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    @ColumnInfo(name = "alarm_time")
    val alarmTime: Long,

    @ColumnInfo(name = "minutes_before")
    val minutesBefore: Int,

    @ColumnInfo(name = "task_type")
    val taskType: String,

    @ColumnInfo(name = "week_days")
    val weekDays: String,

    @ColumnInfo(name = "completed_dates", defaultValue = "")
    val completedDates: String = "",

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "alarm_triggered_at")
    val alarmTriggeredAt: Long?,

    @ColumnInfo(name = "skip_until")
    val skipUntil: Long?,

    @ColumnInfo(name = "sound_uri")
    val soundUri: String?,

    @ColumnInfo(name = "category_id")
    val categoryId: String?,
)

