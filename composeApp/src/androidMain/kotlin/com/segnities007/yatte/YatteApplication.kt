package com.segnities007.yatte

import android.app.Application
import com.segnities007.yatte.data.aggregate.settings.initializeDataStore
import com.segnities007.yatte.data.core.database.initializeDatabase
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import com.segnities007.yatte.domain.aggregate.alarm.usecase.GetScheduledAlarmsUseCase
import com.segnities007.yatte.platform.cleanup.ExpiredTaskCleanupScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

/**
 * Androidアプリケーション初期化。
 *
 * 前提:
 * - DataStore/Room(KMP) はプラットフォーム側の初期化（Context注入）が必要。
 * - KoinでData層を組み立てる前に初期化しないと、DB生成時にクラッシュしうる。
 */
class YatteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // DataStore初期化
        initializeDataStore(this)

        // Room (KMP) のDBビルダーにContextを渡す
        initializeDatabase(this)

        startKoin {
            androidLogger()
            androidContext(this@YatteApplication)
            modules(allModules)
        }

        // 期限切れ削除: 起動時に1回 + 1日1回
        ExpiredTaskCleanupScheduler.enqueueStartup(this)
        ExpiredTaskCleanupScheduler.enqueueDaily(this)

        // DB上の未発火アラームを復元してOS側に再スケジュール（再起動後など）
        CoroutineScope(Dispatchers.Default).launch {
            val koin = GlobalContext.get()
            val getScheduledAlarmsUseCase = koin.get<GetScheduledAlarmsUseCase>()
            val scheduler = koin.get<AlarmScheduler>()
            val alarms = getScheduledAlarmsUseCase().first()
            alarms.forEach { alarm ->
                runCatching { scheduler.schedule(alarm) }
            }
        }
    }
}
