package com.segnities007.yatte.di

import org.koin.core.module.Module

/**
 * 全モジュールのリスト
 */
val appModules: List<Module> = listOf(
    databaseModule,
    repositoryModule,
    useCaseModule,
)

