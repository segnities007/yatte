package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import kotlinx.coroutines.flow.first

/**
 * 履歴エクスポートユースケース
 */
class ExportHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * 履歴をCSV形式でエクスポート
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
     * @return JSON形式の文字列
     */
    suspend fun toJson(): Result<String> = runCatching {
        val histories = repository.getAll().first()
        buildJson(histories)
    }

    private fun buildCsv(histories: List<History>): String {
        val header = "id,taskId,title,completedAt"
        val rows = histories.joinToString("\n") { h ->
            "${h.id.value},${h.taskId.value},\"${h.title.replace("\"", "\"\"")}\",${h.completedAt}"
        }
        return "$header\n$rows"
    }

    private fun buildJson(histories: List<History>): String {
        val items = histories.joinToString(",\n") { h ->
            """  {
    "id": "${h.id.value}",
    "taskId": "${h.taskId.value}",
    "title": "${h.title.replace("\"", "\\\"")}",
    "completedAt": "${h.completedAt}"
  }"""
        }
        return "[\n$items\n]"
    }
}
