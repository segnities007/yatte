package com.segnities007.yatte.platform.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import com.segnities007.yatte.domain.aggregate.alarm.usecase.GetScheduledAlarmsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * デバイス再起動時にアラームを復元するレシーバー。
 * DBに保存されている未発火アラームを取得し、再度OSにスケジュールする。
 */
class BootReceiver :
    BroadcastReceiver(),
    KoinComponent {
    private val getScheduledAlarmsUseCase: GetScheduledAlarmsUseCase by inject()
    private val scheduler: AlarmScheduler by inject()
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        scope.launch {
            try {
                // DBから未発火アラームを取得して再スケジュール
                val alarms = getScheduledAlarmsUseCase().first()
                alarms.forEach { alarm ->
                    runCatching {
                        scheduler.schedule(alarm)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
                scope.cancel()
            }
        }
    }
}
