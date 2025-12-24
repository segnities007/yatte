package com.segnities007.yatte.domain.aggregate.alarm.usecase

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository

/**
 * アラームスケジュールユースケース
 */
class ScheduleAlarmUseCase(
    private val repository: AlarmRepository,
) {
    /**
     * アラームをスケジュールする
     *
     * @param alarm スケジュールするアラーム
     * @return 成功時はUnit、失敗時はエラー
     */
    suspend operator fun invoke(alarm: Alarm): Result<Unit> =
        runCatching {
            repository.schedule(alarm)
        }
}
