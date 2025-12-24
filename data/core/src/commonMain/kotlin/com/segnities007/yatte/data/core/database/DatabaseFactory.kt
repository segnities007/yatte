package com.segnities007.yatte.data.core.database

import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * データベースビルダーを取得する（プラットフォーム固有）
 */
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

/**
 * データベースインスタンスを作成
 */
fun createDatabase(): AppDatabase {
    return getDatabaseBuilder()
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .build()
}
