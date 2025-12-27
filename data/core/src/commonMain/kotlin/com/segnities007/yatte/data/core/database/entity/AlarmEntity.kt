package com.segnities007.yatte.data.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * アラームのDBエンティティ
 */
@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "task_id")
    val taskId: String,

    @ColumnInfo(name = "scheduled_at")
    val scheduledAt: Long,

    @ColumnInfo(name = "notify_at")
    val notifyAt: Long,

    @ColumnInfo(name = "is_triggered")
    val isTriggered: Boolean,

    @ColumnInfo(name = "sound_uri")
    val soundUri: String?,
)
