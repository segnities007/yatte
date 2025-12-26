package com.segnities007.yatte.domain.aggregate.settings.usecase

import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository

/**
 * 全データをリセットする
 *
 * 対象:
 * - 全タスク
 * - 全履歴
 */
class ResetAllDataUseCase(
    private val taskRepository: TaskRepository,
    private val historyRepository: HistoryRepository,
) {
    suspend operator fun invoke(): Result<Unit> =
        runCatching {
            taskRepository.deleteAll()
            historyRepository.deleteAll()
        }
}
