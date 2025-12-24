package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository

class ScheduleAlarmUseCase(
    private val repository: AlarmRepository,
) {
    /**
     * [alarm] をスケジュールする。
     */
    suspend operator fun invoke(alarm: Alarm): Result<Unit> =
        runCatching {
            repository.schedule(alarm)
        }
}
