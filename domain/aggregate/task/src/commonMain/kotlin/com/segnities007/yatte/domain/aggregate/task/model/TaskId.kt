package com.segnities007.yatte.domain.aggregate.task.model

import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * タスクを一意に識別するための値オブジェクト
 */
@JvmInline
value class TaskId(val value: String) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun generate(): TaskId = TaskId(Uuid.random().toString())
    }
}

