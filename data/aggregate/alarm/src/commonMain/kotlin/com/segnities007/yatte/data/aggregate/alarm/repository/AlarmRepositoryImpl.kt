package com.segnities007.yatte.data.aggregate.alarm.repository

import com.segnities007.yatte.data.aggregate.alarm.mapper.toDomain
import com.segnities007.yatte.data.aggregate.alarm.mapper.toEntity
import com.segnities007.yatte.data.core.database.dao.AlarmDao
import com.segnities007.yatte.data.core.database.dao.TaskDao
import com.segnities007.yatte.domain.aggregate.alarm.model.Alarm
import com.segnities007.yatte.domain.aggregate.alarm.model.AlarmId
import com.segnities007.yatte.domain.aggregate.alarm.repository.AlarmRepository
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

/**
 * AlarmRepository の実装（Room永続化）。
 *
 * 注意:
 * - ここでは「DB上の状態」を更新する。
 * - OS側のスケジューリング（AlarmManager等）は別レイヤー（platform側）で扱う。
 */
class AlarmRepositoryImpl(
    private val dao: AlarmDao,
    private val taskDao: TaskDao,
) : AlarmRepository {

    override suspend fun schedule(alarm: Alarm) =
        dao.insert(alarm.toEntity())

    override suspend fun cancel(alarmId: AlarmId) =
        dao.deleteById(alarmId.value)

    override suspend fun cancelByTaskId(taskId: TaskId) =
        dao.deleteByTaskId(taskId.value)

    override fun getScheduledAlarms(): Flow<List<Alarm>> =
        dao.getScheduledAlarms().map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * アラームを発火済みとして永続化し、必要に応じてタスク側にも「発火時刻」を記録する。
     *
     * 仕様:
     * - alarms.is_triggered を更新する（スケジュール済み一覧から除外される）
     * - ONE_TIME タスクの場合は tasks.alarm_triggered_at を更新し、24時間後自動削除の起点にする
     */
    override suspend fun markAsTriggered(alarmId: AlarmId) {
        val alarm = dao.getById(alarmId.value)
        dao.markAsTriggered(alarmId.value)

        val taskId = alarm?.taskId ?: return
        val triggeredAtMillis = Clock.System.now().toEpochMilliseconds()
        taskDao.setAlarmTriggeredAtIfOneTimeTask(taskId = taskId, triggeredAtMillis = triggeredAtMillis)
    }
}
