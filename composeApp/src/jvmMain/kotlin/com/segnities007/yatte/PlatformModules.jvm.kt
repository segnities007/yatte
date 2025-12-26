package com.segnities007.yatte

import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import com.segnities007.yatte.platform.alarm.DesktopAlarmScheduler
import org.koin.core.module.Module
import org.koin.dsl.module

val desktopAlarmModule =
    module {
        single<AlarmScheduler> { DesktopAlarmScheduler() }
    }

actual val platformModules: List<Module> =
    listOf(
        desktopAlarmModule,
    )
