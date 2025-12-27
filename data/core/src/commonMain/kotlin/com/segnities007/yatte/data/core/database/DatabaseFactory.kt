package com.segnities007.yatte.data.core.database

import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * データベースビルダーを取得する（プラットフォーム固有）。
 *
 * 前提:
 * - Androidでは、内部でContextが必要なため事前初期化が必須（androidMain側の実装参照）
 */
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

/**
 * データベースインスタンスを作成する。
 *
 * 仕様:
 * - downgrade（バージョンダウン）時は destructive（テーブル削除）で復旧する
 * - upgrade（バージョンアップ）時も destructive で復旧する（開発中のため）
 */
fun createDatabase(): AppDatabase {
    return getDatabaseBuilder()
        .fallbackToDestructiveMigration(dropAllTables = true)
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .build()
}

