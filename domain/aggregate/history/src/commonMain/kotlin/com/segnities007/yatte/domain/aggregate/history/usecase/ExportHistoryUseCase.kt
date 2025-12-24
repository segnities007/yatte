package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import kotlinx.coroutines.flow.first
class ExportHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * 履歴をCSV形式でエクスポート
     *
     * フォーマット方針:
     * - ヘッダー行 + 明細行（改行区切り）
     * - 文字列フィールドはダブルクォートで囲い、内部の `"` は `""` にエスケープする
     *
     * 注意:
     * - `title` に改行が含まれる場合、CSVビューアの挙動に依存する。
     *
     * @return CSV形式の文字列
     */
    suspend fun toCsv(): Result<String> = runCatching {
        val histories = repository.getAll().first()
        buildCsv(histories)
    }

    /**
     * 履歴をJSON形式でエクスポート
     *
     * 注意:
     * - 外部JSONライブラリを使わず、最小限の文字列エスケープを行う。
     * - JSONのエンコード方針を変更したい場合は、ここを単一の責務として差し替えやすい。
     *
     * @return JSON形式の文字列
     */
    suspend fun toJson(): Result<String> = runCatching {
        val histories = repository.getAll().first()
        buildJson(histories)
    }

    /**
     * CSV文字列を構築する。
     */
    private fun buildCsv(histories: List<History>): String {
        val header = "id,taskId,title,completedAt"
        val rows = histories.joinToString("\n") { h ->
            "${h.id.value},${h.taskId.value},\"${escapeCsv(h.title)}\",${h.completedAt}"
        }
        return "$header\n$rows"
    }

    /**
     * JSON文字列を構築する。
     */
    private fun buildJson(histories: List<History>): String {
        val items = histories.joinToString(",\n") { h ->
            """  {
    "id": "${h.id.value}",
    "taskId": "${h.taskId.value}",
    "title": "${escapeJson(h.title)}",
    "completedAt": "${h.completedAt}"
  }"""
        }
        return "[\n$items\n]"
    }

    private fun escapeCsv(value: String): String = value.replace("\"", "\"\"")

    private fun escapeJson(value: String): String =
        buildString(value.length) {
            value.forEach { ch ->
                when (ch) {
                    '\\' -> append("\\\\")
                    '"' -> append("\\\"")
                    '\n' -> append("\\n")
                    '\r' -> append("\\r")
                    '\t' -> append("\\t")
                    else -> append(ch)
                }
            }
        }
}
