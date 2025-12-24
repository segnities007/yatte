package com.segnities007.yatte.data.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 履歴のDBエンティティ
 */
@Entity(tableName = "histories")
data class HistoryEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "task_id")
    val taskId: String,

    val title: String,

    @ColumnInfo(name = "completed_at")
    val completedAt: Long,
)
