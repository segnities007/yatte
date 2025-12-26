package com.segnities007.yatte.platform.alarm

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler
import com.segnities007.yatte.domain.aggregate.settings.usecase.GetSettingsUseCase
import com.segnities007.yatte.domain.aggregate.task.repository.TaskRepository
import com.segnities007.yatte.platform.audio.DesktopAudioPlayer
import com.segnities007.yatte.platform.notification.DesktopNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.logging.Logger

class DesktopAlarmScheduler :
    AlarmScheduler,
    KoinComponent {
    private val logger = Logger.getLogger(DesktopAlarmScheduler::class.java.name)
    private val scope = CoroutineScope(Dispatchers.Default)
    private val scheduledJobs = mutableMapOf<AlarmId, Job>()

    private val alarmRepository: AlarmRepository by inject()
    private val taskRepository: TaskRepository by inject()
    private val getSettingsUseCase: GetSettingsUseCase by inject()

    override suspend fun schedule(alarm: Alarm) {
        // 既存のジョブがあればキャンセル
        cancel(alarm.id)

        val nowMillis = System.currentTimeMillis()
        val a = alarm.notifyAt
        val jMonth = java.time.Month.valueOf(a.month.name)
        val jLocal = java.time.LocalDateTime.of(a.year, jMonth, a.day, a.hour, a.minute, a.second, a.nanosecond)
        val triggerMillis =
            jLocal
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

        val delayMillis = triggerMillis - nowMillis

        if (delayMillis < 0) {
            logger.warning("[Desktop] Alarm ${alarm.id} is in the past, skipping.")
            return
        }

        logger.info("[Desktop] Scheduled alarm ${alarm.id} in $delayMillis ms")

        val job =
            scope.launch {
                delay(delayMillis)
                triggerAlarm(alarm)
            }
        scheduledJobs[alarm.id] = job
    }

    override suspend fun cancel(alarmId: AlarmId) {
        scheduledJobs[alarmId]?.cancel()
        scheduledJobs.remove(alarmId)
    }

    private suspend fun triggerAlarm(alarm: Alarm) {
        logger.info("[Desktop] Alarm Triggered: ${alarm.id}")

        // リポジトリ更新
        alarmRepository.markAsTriggered(alarm.id)

        // タスク情報取得
        val task = alarm.taskId.let { taskRepository.getById(it) }
        val title = task?.title ?: "Time's up!"

        // 設定取得
        val settings = getSettingsUseCase().first()
        val isSoundEnabled = settings.notificationSound

        // 通知
        DesktopNotification.show("Yatte", "Time for: $title")

        // 音再生
        if (isSoundEnabled) {
            DesktopAudioPlayer.play(settings.customSoundUri)
        }
    }
}
