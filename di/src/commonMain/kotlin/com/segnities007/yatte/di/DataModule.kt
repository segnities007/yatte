package com.segnities007.yatte.di

import com.segnities007.yatte.data.aggregate.alarm.repository.AlarmRepositoryImpl
import com.segnities007.yatte.data.aggregate.history.repository.HistoryRepositoryImpl
import com.segnities007.yatte.data.aggregate.task.repository.TaskRepositoryImpl
import com.segnities007.yatte.data.core.database.AppDatabase
import com.segnities007.yatte.data.core.database.createDatabase
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.history.repository.HistoryRepository
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import org.koin.dsl.module

/**
 * データベースモジュール
 */
val databaseModule = module {
    single<AppDatabase> { createDatabase() }
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().historyDao() }
    single { get<AppDatabase>().alarmDao() }
}

/**
 * リポジトリモジュール
 */
val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
}
