package com.segnities007.yatte

import com.segnities007.yatte.di.appModules
import com.segnities007.yatte.presentation.feature.category.di.categoryFeatureModule
import com.segnities007.yatte.presentation.feature.history.di.historyViewModelModule
import com.segnities007.yatte.presentation.feature.home.di.homeViewModelModule
import com.segnities007.yatte.presentation.feature.management.di.taskManagementViewModelModule
import com.segnities007.yatte.presentation.feature.settings.di.settingsViewModelModule
import com.segnities007.yatte.presentation.feature.task.di.taskViewModelModule
import com.segnities007.yatte.presentation.navigation.di.navigationModule
import org.koin.core.module.Module

/**
 * 全てのKoinモジュールを集約
 */
val allModules: List<Module> =
    appModules +
        platformModules +
        listOf(
            navigationModule,
            homeViewModelModule,
            taskViewModelModule,
            historyViewModelModule,
            settingsViewModelModule,
            taskManagementViewModelModule,
            categoryFeatureModule,
        )
