package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

/**
 * スケジュール済みアラーム取得ユースケース
 */
class GetScheduledAlarmsUseCase(
    private val repository: AlarmRepository,
) {
    /**
     * スケジュール済みのアラーム一覧をFlowで取得
     *
     * @return アラームのFlowリスト
     */
    operator fun invoke(): Flow<List<Alarm>> = repository.getScheduledAlarms()
}
