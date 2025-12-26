package com.segnities007.yatte.platform.cleanup

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.platform.AndroidKoinBootstrapper
import org.koin.core.context.GlobalContext

/**
 * 期限切れタスク（アラーム発火から24h経過）を削除するワーカー。
 *
 * 仕様:
 * - 起動時に1回
 * - 1日1回の定期実行
 */
class ExpiredTaskCleanupWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        AndroidKoinBootstrapper.ensureStarted(applicationContext)

        val taskRepository = GlobalContext.get().get<TaskRepository>()
        return runCatching {
            taskRepository.deleteExpiredTasks()
        }.fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() },
        )
    }
}
