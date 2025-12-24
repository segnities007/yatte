package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
class GetScheduledAlarmsUseCase(
    private val repository: AlarmRepository,
) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getScheduledAlarms()
}
