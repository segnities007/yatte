package com.segnities007.yatte.platform.di

import android.content.Context
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import com.segnities007.yatte.platform.alarm.AndroidAlarmScheduler
import org.koin.dsl.module

val androidPlatformModule =
    module {
        single<AlarmScheduler> { AndroidAlarmScheduler(get<Context>()) }
    }
