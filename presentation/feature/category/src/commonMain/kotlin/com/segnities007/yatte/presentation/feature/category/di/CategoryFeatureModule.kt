package com.segnities007.yatte.presentation.feature.category.di

import com.segnities007.yatte.presentation.feature.category.CategoryViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryFeatureModule = module {
    viewModel { CategoryViewModel(get(), get(), get()) }
}
