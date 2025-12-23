package com.segnites007.yatte

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "yatte",
        ) {
            App()
        }
    }
