package com.segnities007.yatte.domain.aggregate.history.model

/**
 * 履歴のステータス（タスクの終了理由）
 */
enum class HistoryStatus {
    /** 完了 */
    COMPLETED,
    /** スキップ */
    SKIPPED,
    /** 時間切れ（自動削除） */
    EXPIRED,
}
