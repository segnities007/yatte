package com.segnities007.yatte

import com.segnities007.yatte.di.alarmSchedulerModule
import org.koin.core.module.Module

actual val platformModules: List<Module> =
    listOf(
        // Phase-1: iOSはMac無し前提で後回し（ログのみ）
        alarmSchedulerModule,
    )
