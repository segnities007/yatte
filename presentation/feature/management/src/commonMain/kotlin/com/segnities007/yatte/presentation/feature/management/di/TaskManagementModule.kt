package com.segnities007.yatte.presentation.feature.management.di

import com.segnities007.yatte.presentation.feature.management.TaskManagementViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val taskManagementViewModelModule = module {
    viewModel { TaskManagementViewModel(get(), get(), get()) }
}
