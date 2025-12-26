package com.segnities007.yatte.domain.aggregate.alarm.scheduler

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId

/**
 * アラームをOSにスケジュールするためのインターフェース。
 *
 * プラットフォーム固有の実装:
 * - Android: AlarmManager / WorkManager
 * - iOS: UNUserNotificationCenter
 * - Desktop: Coroutines + システム通知
 */
interface AlarmScheduler {
    /**
     * 指定された[alarm]をスケジュールする。
     */
    suspend fun schedule(alarm: Alarm)

    /**
     * 指定された[alarmId]のアラームをキャンセルする。
     */
    suspend fun cancel(alarmId: AlarmId)
}
