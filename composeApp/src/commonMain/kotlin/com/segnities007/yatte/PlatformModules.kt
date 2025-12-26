package com.segnities007.yatte

import org.koin.core.module.Module

/**
 * プラットフォーム固有のKoinモジュール。
 *
 * 例:
 * - Android: AlarmScheduler/WorkManager/Notification などのOS連携
 * - iOS: UNUserNotificationCenter などのOS連携（Phase-2以降）
 * - JVM: Desktop通知など（将来）
 */
expect val platformModules: List<Module>
