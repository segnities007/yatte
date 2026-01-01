package com.segnities007.yatte.domain.aggregate.category.model

import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * タスクカテゴリのドメインモデル
 *
 * @property id カテゴリの一意識別子
 * @property name カテゴリ名（1文字以上50文字以下）
 * @property color カテゴリの色
 * @property createdAt 作成日時
 */
data class Category(
    val id: CategoryId,
    val name: String,
    val color: CategoryColor,
    val createdAt: LocalDateTime,
) {
    init {
        require(name.isNotBlank()) { "カテゴリ名は必須です" }
        require(name.length <= MAX_NAME_LENGTH) { "カテゴリ名は${MAX_NAME_LENGTH}文字以下にしてください" }
    }

    companion object {
        private const val MAX_NAME_LENGTH = 50

        /**
         * 新しいカテゴリを作成する
         */
        fun create(
            name: String,
            color: CategoryColor,
        ): Category {
            return Category(
                id = CategoryId.generate(),
                name = name,
                color = color,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            )
        }
    }
}

enum class CategoryColor(val hex: Long) {
    RED(0xFFFFCDD2),
    PINK(0xFFF8BBD0),
    PURPLE(0xFFE1BEE7),
    DEEP_PURPLE(0xFFD1C4E9),
    INDIGO(0xFFC5CAE9),
    BLUE(0xFFBBDEFB),
    LIGHT_BLUE(0xFFB3E5FC),
    CYAN(0xFFB2EBF2),
    TEAL(0xFFB2DFDB),
    GREEN(0xFFC8E6C9),
    LIGHT_GREEN(0xFFDCEDC8),
    LIME(0xFFF0F4C3),
    YELLOW(0xFFFFF9C4),
    AMBER(0xFFFFECB3),
    ORANGE(0xFFFFE0B2),
    DEEP_ORANGE(0xFFFFCCBC),
    BROWN(0xFFD7CCC8),
    GREY(0xFFF5F5F5),
    BLUE_GREY(0xFFCFD8DC),
    ;
    
    companion object {
        fun fromHex(hex: Long): CategoryColor = entries.find { it.hex == hex } ?: GREY
    }
}
