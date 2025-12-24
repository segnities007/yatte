package com.segnities007.yatte.di

import com.segnities007.yatte.data.aggregate.alarm.repository.AlarmRepositoryImpl
import com.segnities007.yatte.data.aggregate.history.repository.HistoryRepositoryImpl
import com.segnities007.yatte.data.aggregate.settings.createDataStore
import com.segnities007.yatte.data.aggregate.settings.repository.SettingsRepositoryImpl
import com.segnities007.yatte.data.aggregate.task.repository.TaskRepositoryImpl
import com.segnities007.yatte.data.core.database.AppDatabase
import com.segnities007.yatte.data.core.database.createDatabase
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import com.segnities007.yatte.domain.aggregate.settings.repository.SettingsRepository
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import org.koin.dsl.module

/**
 * データベースモジュール。
 *
 * 前提:
 * - Androidでは `createDatabase()` 実行前に `initializeDatabase(context)` が必要。
 *   （Application起動時に初期化してからKoinを開始する）
 */
val databaseModule = module {
    single<AppDatabase> { createDatabase() }
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().historyDao() }
    single { get<AppDatabase>().alarmDao() }
    single { createDataStore() }
}

/**
 * リポジトリモジュール。
 *
 * Data層のRepositoryImplは、Domain層のRepositoryインターフェースを実装し、
 * Entity↔Model変換やクエリ条件（例: 今日のタスク、期限切れ削除）を担当する。
 */
val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
    single<AlarmRepository> { AlarmRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}
