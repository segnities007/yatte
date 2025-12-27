package com.segnities007.yatte.presentation.feature.home.di

import com.segnities007.yatte.presentation.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Home FeatureのViewModelモジュール
 */
val homeViewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get()) }
}
