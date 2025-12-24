package com.segnites007.yatte

import android.app.Application
import com.segnities007.yatte.data.core.database.createDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class YatteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val dbModule =
            module {
                single { createDatabase(this@YatteApplication) }
            }

        startKoin {
            androidLogger()
            androidContext(this@YatteApplication)
            modules(allModules + dbModule)
        }
    }
}
