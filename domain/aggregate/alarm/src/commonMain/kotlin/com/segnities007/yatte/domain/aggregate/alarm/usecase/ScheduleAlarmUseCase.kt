package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.alarm.scheduler.AlarmScheduler

class ScheduleAlarmUseCase(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler,
) {
    /**
     * [alarm] をスケジュールする。
     * 1. DBに永続化
     * 2. OSにスケジュール
     */
    suspend operator fun invoke(alarm: Alarm): Result<Unit> =
        runCatching {
            repository.schedule(alarm)
            scheduler.schedule(alarm)
        }
}

