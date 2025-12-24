package com.segnities007.yatte.data.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private lateinit var appContext: Context

/**
 * Room(KMP) のDBビルダー作成に必要なContextを初期化する。
 *
 * 前提:
 * - DIなどから `createDatabase()`（commonMain）を呼ぶ前に必ず実行する
 * - `applicationContext` を保持し、Activityのライフサイクルに依存しないようにする
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
 * Android用のデータベース作成（Contextあり）。
 *
 * - 内部で `initializeDatabase()` を呼んだ上で commonMain の `createDatabase()` を実行する
 */
fun createDatabase(context: Context): AppDatabase {
    initializeDatabase(context)
    return createDatabase()
}
