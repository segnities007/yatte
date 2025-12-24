package com.segnites007.yatte

import android.app.Application
import com.segnities007.yatte.data.aggregate.settings.initializeDataStore
import com.segnities007.yatte.data.core.database.initializeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
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
    }
}
