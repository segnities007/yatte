package com.segnities007.yatte.domain.aggregate.alarm.repository

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.coroutines.flow.Flow

/**
 * アラームリポジトリのインターフェース
 *
 * プラットフォーム固有の実装が必要
 */
interface AlarmRepository {
    /**
     * アラームをスケジュール
     */
    suspend fun schedule(alarm: Alarm)

    /**
     * IDでアラームをキャンセル
     */
    suspend fun cancel(alarmId: AlarmId)

    /**
     * タスクIDに紐づくアラームをすべてキャンセル
     */
    suspend fun cancelByTaskId(taskId: TaskId)

    /**
     * スケジュール済みのアラーム一覧を取得
     */
    fun getScheduledAlarms(): Flow<List<Alarm>>

    /**
     * アラームを発火済みにする
     */
    suspend fun markTriggered(alarmId: AlarmId)
}


