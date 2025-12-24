package com.segnities007.yatte.domain.aggregate.alarm.model

import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * アラームを一意に識別するための値オブジェクト
 */
@JvmInline
value class AlarmId(val value: String) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun generate(): AlarmId = AlarmId(Uuid.random().toString())
    }
}
