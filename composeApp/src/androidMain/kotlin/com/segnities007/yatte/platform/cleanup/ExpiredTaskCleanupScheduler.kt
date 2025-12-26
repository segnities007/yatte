package com.segnities007.yatte.platform.cleanup

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

internal object ExpiredTaskCleanupScheduler {
    private const val UNIQUE_PERIODIC_NAME = "expired_task_cleanup_daily"
    private const val UNIQUE_STARTUP_NAME = "expired_task_cleanup_startup"

    fun enqueueStartup(context: Context) {
        val request = OneTimeWorkRequestBuilder<ExpiredTaskCleanupWorker>().build()
        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(UNIQUE_STARTUP_NAME, ExistingWorkPolicy.KEEP, request)
    }

    fun enqueueDaily(context: Context) {
        val request =
            PeriodicWorkRequestBuilder<ExpiredTaskCleanupWorker>(1, TimeUnit.DAYS)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request,
            )
    }
}
