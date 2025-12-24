package com.segnities007.yatte.domain.aggregate.task.model

/**
 * タスクの種類
 */
enum class TaskType {
    /**
     * 1回限りのタスク
     * アラーム後24時間で自動削除
     */
    ONE_TIME,

    /**
     * 毎週繰り返すルーチンタスク
     * 指定した曜日に繰り返し
     */
    WEEKLY_LOOP
}
