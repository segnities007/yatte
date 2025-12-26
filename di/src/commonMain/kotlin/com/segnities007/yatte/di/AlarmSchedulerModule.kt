package com.segnities007.yatte.di

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import org.koin.dsl.module

/**
 * AlarmScheduler のモジュール。
 * 仮実装として LogOnlyAlarmScheduler を提供。
 * プラットフォーム固有実装は後続タスクで追加。
 */
val alarmSchedulerModule = module {
    single<AlarmScheduler> { LogOnlyAlarmScheduler() }
}

/**
 * ログ出力のみのAlarmScheduler仮実装。
 * 実際のOS通知はスケジュールしない。
 */
class LogOnlyAlarmScheduler : AlarmScheduler {
    override suspend fun schedule(alarm: Alarm) {
        println("[AlarmScheduler] SCHEDULED: id=${alarm.id.value}, scheduledAt=${alarm.scheduledAt}")
    }

    override suspend fun cancel(alarmId: AlarmId) {
        println("[AlarmScheduler] CANCELLED: id=${alarmId.value}")
    }
}
