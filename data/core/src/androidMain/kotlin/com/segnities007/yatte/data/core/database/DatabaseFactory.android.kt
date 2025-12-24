package com.segnities007.yatte.data.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private lateinit var appContext: Context

/**
 * Android用のContextを設定
 */
fun initializeDatabase(context: Context) {
    appContext = context.applicationContext
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME,
    )
}

/**
 * Android用のデータベース作成（Contextあり）
 */
fun createDatabase(context: Context): AppDatabase {
    initializeDatabase(context)
    return createDatabase()
}
