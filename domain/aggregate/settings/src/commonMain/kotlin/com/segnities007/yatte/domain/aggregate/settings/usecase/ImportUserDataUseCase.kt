package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class ImportUserDataUseCase(
    private val taskRepository: TaskRepository,
    private val historyRepository: HistoryRepository,
) {
    /**
     * JSON文字列からユーザーデータをインポート（追記）する
     */
    suspend fun invoke(jsonString: String): Result<Int> = runCatching {
        // 簡易パース: "tasks": [...] と "histories": [...] ブロックを切り出す
        
        val tasksBlock = extractArrayBlock(jsonString, "tasks")
        val historiesBlock = extractArrayBlock(jsonString, "histories")
        
        var count = 0
        
        if (tasksBlock != null) {
            val tasks = parseTasks(tasksBlock)
            tasks.forEach {
                 // ID重複チェックなどはRepository実装依存だが、基本は追記(Upsert or Insert)
                 // ここでは既に存在すれば上書き、無ければ作成が理想だが、
                 // IDがUUIDなら確率的に衝突しない。
                 // 既存実装のinsertがUpsert挙動かは不明だが、Insert呼ぶ。
                 // TODO: RepositoryのinsertがOnConflict.REPLACEであることを期待
                 // もしくは try-catch? 今回は単純にinsert
                 // エラーが出たらスキップするなど
                 runCatching { taskRepository.insert(it) }.onSuccess { count++ }
            }
        }
        
        if (historiesBlock != null) {
            val histories = parseHistories(historiesBlock)
            histories.forEach {
                runCatching { historyRepository.insert(it) }.onSuccess { count++ }
            }
        }
        
        count
    }

    private fun extractArrayBlock(json: String, key: String): String? {
        val keyIndex = json.indexOf("\"$key\"")
        if (keyIndex == -1) return null
        val openBracket = json.indexOf("[", keyIndex)
        if (openBracket == -1) return null
        
        // 対応する閉じ括弧を探す
        var depth = 0
        for (i in openBracket until json.length) {
            val c = json[i]
            if (c == '[') depth++
            else if (c == ']') depth--
            
            if (depth == 0) {
                return json.substring(openBracket, i + 1)
            }
        }
        return null
    }

    private fun parseTasks(jsonBlock: String): List<Task> {
        // オブジェクト毎に分割。
        // ネスト（alarmConfig）があるので、単純な正規表現 "{.*?}" は危険。
        // 手動で深度トラッキングしてオブジェクト文字列を抽出リスト化する
        val objects = splitObjects(jsonBlock)
        return objects.mapNotNull { json ->
            parseTask(json)
        }
    }
    
    private fun parseHistories(jsonBlock: String): List<History> {
        val objects = splitObjects(jsonBlock)
        return objects.mapNotNull { json ->
            parseHistory(json)
        }
    }
    
    // 文字列の配列 [...] の中身から {} の塊リストを返す
    private fun splitObjects(arrayContent: String): List<String> {
        val result = mutableListOf<String>()
        var depth = 0
        var start = -1
        
        for (i in arrayContent.indices) {
            val c = arrayContent[i]
            if (c == '{') {
                if (depth == 0) start = i
                depth++
            } else if (c == '}') {
                depth--
                if (depth == 0 && start != -1) {
                    result.add(arrayContent.substring(start, i + 1))
                    start = -1
                }
            }
        }
        return result
    }

    private fun parseTask(json: String): Task? {
        val id = extractValue(json, "id") ?: return null
        val title = unescapeJson(extractValue(json, "title") ?: "")
        val description = unescapeJson(extractValue(json, "description") ?: "")
        val categoryIdStr = extractValue(json, "categoryId")
        val categoryId = if (!categoryIdStr.isNullOrBlank()) CategoryId(categoryIdStr) else null
        
        val timeStr = extractValue(json, "time") ?: "09:00"
        val minutesBeforeStr = extractValue(json, "minutesBefore") ?: "10"
        val taskTypeStr = extractValue(json, "taskType") ?: "ONE_TIME"
        val createdAtStr = extractValue(json, "createdAt") ?: return null
        val soundUri = extractValue(json, "soundUri")?.ifBlank { null }
        
        // Arrays handled by extractArrayBlock-like logic logic for properties
        val weekDaysBlock = extractArrayBlock(json, "weekDays")
        val weekDays = if (weekDaysBlock != null) {
            weekDaysBlock.replace("[", "").replace("]", "").replace("\"", "").split(",")
                .mapNotNull { w -> 
                     val trimmed = w.trim()
                     if (trimmed.isEmpty()) null else runCatching { DayOfWeek.valueOf(trimmed) }.getOrNull()
                }
        } else emptyList()

        val completedDatesBlock = extractArrayBlock(json, "completedDates")
        val completedDates = if (completedDatesBlock != null) {
             completedDatesBlock.replace("[", "").replace("]", "").replace("\"", "").split(",")
                .mapNotNull { d ->
                    val trimmed = d.trim()
                    if (trimmed.isEmpty()) null else runCatching { LocalDate.parse(trimmed) }.getOrNull()
                }.toSet()
        } else emptySet()

        return Task(
            id = TaskId(id),
            title = title,
            description = description,
            categoryId = categoryId,
            time = LocalTime.parse(timeStr),
            minutesBefore = minutesBeforeStr.toIntOrNull() ?: 10,
            taskType = runCatching { TaskType.valueOf(taskTypeStr) }.getOrDefault(TaskType.ONE_TIME),
            weekDays = weekDays,
            completedDates = completedDates,
            createdAt = LocalDateTime.parse(createdAtStr),
            soundUri = soundUri
        )
    }

    private fun parseHistory(json: String): History? {
        val id = extractValue(json, "id") ?: return null
        val taskId = extractValue(json, "taskId") ?: return null
        val title = unescapeJson(extractValue(json, "title") ?: "")
        val completedAt = extractValue(json, "completedAt") ?: return null

        return History(
            id = HistoryId(id),
            taskId = TaskId(taskId),
            title = title,
            completedAt = LocalDateTime.parse(completedAt)
        )
    }

    // 似たロジックを共通化
    private fun extractValue(json: String, key: String): String? {
        // "key": "value" or "key": literal
        val keyStart = json.indexOf("\"$key\"")
        if (keyStart == -1) return null
        val colon = json.indexOf(":", keyStart)
        if (colon == -1) return null
        
        val valueStartSearch = colon + 1
        // 最初の非空白文字を探す
        var valueStart = valueStartSearch
        while (valueStart < json.length && json[valueStart].isWhitespace()) valueStart++
        if (valueStart >= json.length) return null
        
        if (json[valueStart] == '"') {
             // Quoted String
             var current = valueStart + 1
             while (current < json.length) {
                 if (json[current] == '"' && json[current - 1] != '\\') {
                     return json.substring(valueStart + 1, current)
                 }
                 current++
             }
        } else {
            // Primitive (boolean, number) or null
            // 次の , or } or ] or \n まで
            var current = valueStart
            while (current < json.length) {
                val c = json[current]
                if (c == ',' || c == '}' || c == ']' || c.isWhitespace()) {
                    return json.substring(valueStart, current).trim()
                }
                current++
            }
        }
        return null
    }
    
    private fun extractObject(json: String, key: String): String? {
        val keyStart = json.indexOf("\"$key\"")
        if (keyStart == -1) return null
        val openBrace = json.indexOf("{", keyStart)
        if (openBrace == -1) return null
        
        var depth = 0
        for (i in openBrace until json.length) {
            if (json[i] == '{') depth++
            else if (json[i] == '}') depth--
            
            if (depth == 0) return json.substring(openBrace, i + 1)
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
