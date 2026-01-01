package com.segnities007.yatte.data.aggregate.history.local

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

    @ColumnInfo(name = "status", defaultValue = "COMPLETED")
    val status: String = "COMPLETED",
)

