package com.segnities007.yatte.presentation.feature.settings.di

import com.segnities007.yatte.presentation.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Settings FeatureのViewModelモジュール
 */
val settingsViewModelModule = module {
    viewModel { SettingsViewModel(get(), get()) }
}
