package com.segnities007.yatte.domain.aggregate.history.model

import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.datetime.LocalDateTime

/**
 * 完了したタスクの履歴
 *
 * @property id 履歴の一意識別子
 * @property taskId 元のタスクID
 * @property title タスクのタイトル（1文字以上）
 * @property completedAt 完了日時
 */
data class History(
    val id: HistoryId,
    val taskId: TaskId,
    val title: String,
    val completedAt: LocalDateTime,
) {
    init {
        require(title.isNotBlank()) { "タイトルは必須です" }
    }
}


