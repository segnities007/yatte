package com.segnities007.yatte.data.aggregate.category.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * カテゴリのDBエンティティ
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    @ColumnInfo(name = "color_hex")
    val colorHex: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
