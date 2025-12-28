package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import kotlinx.coroutines.flow.first

class ExportUserDataUseCase(
    private val taskRepository: TaskRepository,
    private val historyRepository: HistoryRepository,
) {
    /**
     * ユーザーデータ（タスク・履歴）をJSON形式でエクスポート
     */
    suspend fun invoke(): Result<String> = runCatching {
        val tasks = taskRepository.getAllTasks().first()
        val histories = historyRepository.getAll().first()
        buildJson(tasks, histories)
    }

    private fun buildJson(tasks: List<Task>, histories: List<History>): String {
        // Tasks
        val tasksJson = tasks.joinToString(",\n") { t ->
            val weekdays = t.weekDays.joinToString(",") { "\"$it\"" }
            val completedDates = t.completedDates.joinToString(",") { "\"$it\"" }
            """    {
      "id": "${t.id.value}",
      "title": "${escapeJson(t.title)}",
      "description": "${escapeJson(t.description)}",
      "categoryId": "${t.categoryId?.value ?: ""}",
      "time": "${t.time}",
      "minutesBefore": ${t.minutesBefore},
      "taskType": "${t.taskType}",
      "weekDays": [$weekdays],
      "completedDates": [$completedDates],
      "createdAt": "${t.createdAt}",
      "soundUri": "${t.soundUri ?: ""}"
    }"""
        }

        // Histories
        val historiesJson = histories.joinToString(",\n") { h ->
            """    {
      "id": "${h.id.value}",
      "taskId": "${h.taskId.value}",
      "title": "${escapeJson(h.title)}",
      "completedAt": "${h.completedAt}"
    }"""
        }

        return """{
  "version": 1,
  "tasks": [
$tasksJson
  ],
  "histories": [
$historiesJson
  ]
}"""
    }

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
