package com.segnities007.yatte.domain.aggregate.history.model

import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * 履歴を一意に識別するための値オブジェクト
 */
@JvmInline
value class HistoryId(val value: String) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun generate(): HistoryId = HistoryId(Uuid.random().toString())
    }
}

