package com.segnities007.yatte.domain.aggregate.history.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.datetime.LocalDateTime

class ImportHistoryUseCase(
    private val repository: HistoryRepository,
) {
    /**
     * JSON文字列から履歴をインポート（追記）する
     *
     * @param jsonString エクスポート機能で出力されたJSON文字列
     * @return 成功件数
     */
    suspend operator fun invoke(jsonString: String): Result<Int> = runCatching {
        val histories = parseJson(jsonString)
        histories.forEach { history ->
            repository.insert(history)
        }
        histories.size
    }

    private fun parseJson(jsonString: String): List<History> {
        // 簡易的なパース処理。厳密なJSONパーサではないが、ExportHistoryUseCaseの出力形式に合わせる。
        // "key": "value" の形式を想定。
        // 配列 [...] で囲まれている。

        val items = mutableListOf<History>()
        // オブジェクト {} 単位で分割したいが、ネストがない前提で簡易的に行う。
        // "id": "..." を探す。

        // 正規表現で要素を抽出
        // 簡易実装: 全体の文字列から正規表現で各オブジェクトのマッチを探す
        // 注意: タイトルにエスケープされた " が含まれる場合の考慮が必要
        // Export: "title": "${escapeJson(h.title)}"

        // 各オブジェクトブロック {...} を抽出する正規表現
        // 非貪欲マッチ (.*?) を使用
        val objectRegex = Regex("""\{([^{}]*)\}""")
        
        val matches = objectRegex.findAll(jsonString)
        
        matches.forEach { matchResult ->
            val content = matchResult.groupValues[1]
            val id = extractValue(content, "id")
            val taskId = extractValue(content, "taskId")
            val title = extractValue(content, "title")
            val completedAt = extractValue(content, "completedAt")

            if (id != null && taskId != null && title != null && completedAt != null) {
                items.add(
                    History(
                        id = HistoryId(id),
                        taskId = TaskId(taskId),
                        title = unescapeJson(title),
                        completedAt = LocalDateTime.parse(completedAt)
                    )
                )
            }
        }
        
        return items
    }

    private fun extractValue(content: String, key: String): String? {
        // "key": "value"
        // valueはダブルクォートで囲まれている前提。
        // エスケープされたダブルクォート \" を考慮して、"..." の中身を取り出すのが理想だが、
        // 簡易的に "key":\s*"(.*?)(?<!\\)" で取る。（バックスラッシュでエスケープされていない末尾のダブルクォートまで）
        // しかし (?<!\\) はJS/Kotlin JS等でサポートされない場合があるが、JVM/AndroidならOK。
        // ここでは commonMain なので安全策として、単純に "key":\s*" から次の " まで、ただし \" はスキップするロジックが必要。
        

        
        // もう少し堅牢な手動パース
        val keyStart = content.indexOf("\"$key\"")
        if (keyStart == -1) return null
        
        // コロンを探す
        val colonIndex = content.indexOf(":", keyStart)
        if (colonIndex == -1) return null
        
        // 最初の " (値の開始)
        val valueQuoteStart = content.indexOf("\"", colonIndex)
        if (valueQuoteStart == -1) return null
        
        // 値の終了 " を探す（エスケープされていないもの）
        var current = valueQuoteStart + 1
        while (current < content.length) {
            if (content[current] == '"' && content[current - 1] != '\\') {
                return content.substring(valueQuoteStart + 1, current)
            }
            current++
        }
        
        return null
    }

    private fun unescapeJson(value: String): String {
        return value
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
    }
}
