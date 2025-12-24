package com.segnities007.yatte.presentation.feature.history.di

import com.segnities007.yatte.presentation.feature.history.HistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * History FeatureのViewModelモジュール
 */
val historyViewModelModule = module {
    viewModel { HistoryViewModel(get(), get(), get(), get()) }
}
