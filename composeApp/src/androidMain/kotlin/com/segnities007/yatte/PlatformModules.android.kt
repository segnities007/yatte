package com.segnities007.yatte

import com.segnities007.yatte.platform.di.androidPlatformModule
import org.koin.core.module.Module

actual val platformModules: List<Module> =
    listOf(
        androidPlatformModule,
    )
