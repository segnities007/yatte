package com.segnities007.yatte.domain.aggregate.alarm.repository

import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.coroutines.flow.Flow

/**
 * アラームリポジトリのインターフェース
 *
 * 「アラームのスケジュール状態」を永続化するための抽象。
 *
 * 注意:
 * - このリポジトリは「OSのスケジューラ（AlarmManager/UNUserNotificationCenter等）」の実行保証までは担保しない。
 * - OSスケジュール連携を行う場合は、別途プラットフォーム層でアダプタを用意して統合する。
 */
interface AlarmRepository {
    suspend fun schedule(alarm: Alarm)

    /**
     * 指定した [alarmId] のアラームを取得する。
     *
     * 用途:
     * - AndroidのReceiver等、プラットフォーム層で発火時に参照する
     */
    suspend fun getById(alarmId: AlarmId): Alarm?

    /**
     * 指定した [taskId] に紐づく未発火（is_triggered = 0）のアラームを取得する。
     *
     * 用途:
     * - タスク完了/削除/編集時に、OS側のスケジュールもキャンセルする
     */
    suspend fun getByTaskId(taskId: TaskId): Alarm?

    suspend fun cancel(alarmId: AlarmId)

    suspend fun cancelByTaskId(taskId: TaskId)

    fun getScheduledAlarms(): Flow<List<Alarm>>

    /**
     * アラームを「発火済み」として永続化する。
     *
     * 仕様:
     * - アラームの状態はUI状態とは別に永続化される（SSOTはDB）。
     * - 1回限りタスクの場合、24時間後自動削除の起点（発火時刻）更新に利用される。
     */
    suspend fun markAsTriggered(alarmId: AlarmId)
}


