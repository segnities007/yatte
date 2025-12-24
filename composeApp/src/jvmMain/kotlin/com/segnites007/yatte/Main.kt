package com.segnites007.yatte

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(allModules)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "yatte",
        ) {
            App()
        }
    }
}
