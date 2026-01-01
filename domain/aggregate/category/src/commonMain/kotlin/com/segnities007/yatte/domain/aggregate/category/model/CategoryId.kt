package com.segnities007.yatte.domain.aggregate.category.model

import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * カテゴリを一意に識別するための値オブジェクト
 */
@JvmInline
value class CategoryId(val value: String) {
    companion object {
        /**
         * 新しいカテゴリIDを生成する
         */
        @OptIn(ExperimentalUuidApi::class)
        fun generate(): CategoryId = CategoryId(Uuid.random().toString())
    }
}
