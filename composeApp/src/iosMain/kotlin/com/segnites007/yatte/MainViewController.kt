package com.segnites007.yatte

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(allModules)
    }
}

fun mainViewController() = ComposeUIViewController { App() }
