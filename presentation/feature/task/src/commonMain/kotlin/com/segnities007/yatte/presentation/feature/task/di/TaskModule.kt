package com.segnities007.yatte.presentation.feature.task.di

import com.segnities007.yatte.presentation.feature.task.TaskFormViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Task FeatureのViewModelモジュール
 */
val taskViewModelModule = module {
    viewModel { TaskFormViewModel(get(), get(), get(), get(), get(), get(), get()) }
}
