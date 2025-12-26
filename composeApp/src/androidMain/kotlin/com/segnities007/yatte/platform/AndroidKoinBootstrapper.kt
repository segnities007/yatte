package com.segnities007.yatte.platform

import android.content.Context
import com.segnities007.yatte.allModules
import com.segnities007.yatte.data.aggregate.settings.initializeDataStore
import com.segnities007.yatte.data.core.database.initializeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

/**
 * Receiver/Worker など、Applicationより先に起動される可能性があるAndroidコンポーネントから
 * Koinを安全に初期化するためのユーティリティ。
 */
internal object AndroidKoinBootstrapper {
    fun ensureStarted(context: Context) {
        if (GlobalContext.getOrNull() != null) return

        val appContext = context.applicationContext

        // DataStore/Room(KMP) は Context 初期化が必要
        initializeDataStore(appContext)
        initializeDatabase(appContext)

        startKoin {
            androidLogger()
            androidContext(appContext)
            modules(allModules)
        }
    }
}
