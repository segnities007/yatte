package com.segnities007.yatte.domain.aggregate.alarm.model

import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.datetime.LocalDateTime

/**
 * アラームのドメインモデル
 *
 * @property id アラームの一意識別子
 * @property taskId 関連するタスクのID
 * @property scheduledAt アラームが発火する予定日時
 * @property notifyAt 通知する日時（scheduledAtのminutesBefore分前）
 * @property isTriggered アラームが発火済みかどうか
 */
data class Alarm(
    val id: AlarmId,
    val taskId: TaskId,
    val scheduledAt: LocalDateTime,
    val notifyAt: LocalDateTime,
    val isTriggered: Boolean = false,
    val soundUri: String? = null,
) {
    init {
        require(notifyAt <= scheduledAt) { "通知時刻は予定時刻より前である必要があります" }
    }
    fun markAsTriggered(): Alarm = copy(isTriggered = true)
}


