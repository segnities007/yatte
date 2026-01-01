package com.segnities007.yatte.data.aggregate.category.mapper

import com.segnities007.yatte.data.aggregate.category.local.CategoryEntity
import com.segnities007.yatte.domain.aggregate.category.model.Category
import com.segnities007.yatte.domain.aggregate.category.model.CategoryColor
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * CategoryEntity -> Category (Domain Model)
 */
fun CategoryEntity.toDomain(): Category {
    val timeZone = TimeZone.currentSystemDefault()
    return Category(
        id = CategoryId(id),
        name = name,
        color = CategoryColor.fromHex(colorHex),
        createdAt = Instant.fromEpochMilliseconds(createdAt).toLocalDateTime(timeZone),
    )
}

/**
 * Category (Domain Model) -> CategoryEntity
 */
fun Category.toEntity(): CategoryEntity {
    val timeZone = TimeZone.currentSystemDefault()
    return CategoryEntity(
        id = id.value,
        name = name,
        colorHex = color.hex,
        createdAt = createdAt.toInstant(timeZone).toEpochMilliseconds(),
    )
}
