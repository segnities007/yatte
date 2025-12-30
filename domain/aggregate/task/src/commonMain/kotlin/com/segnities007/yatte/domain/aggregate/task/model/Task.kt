package com.segnities007.yatte.domain.aggregate.task.model

/**
 * タスクのドメインモデル
 *
 * @property id タスクの一意識別子
 * @property title タスクのタイトル（1文字以上100文字以下）
 * @property time アラームを鳴らす時刻
 * @property minutesBefore アラームの何分前に通知するか（0以上60以下）
 * @property taskType タスクの種類（1回限り or 毎週繰り返し）
 * @property weekDays 繰り返しの曜日（WEEKLY_LOOPの場合は1つ以上必須）
 * @property completedDates 完了した日付のセット（日付ごとの完了状態を管理）
 * @property createdAt 作成日時
 * @property alarmTriggeredAt アラームが発火した日時（nullの場合は未発火）
 * @property skipUntil この日付までスキップ（週次タスク用、nullの場合はスキップなし）
 */
import com.segnities007.yatte.domain.aggregate.category.model.CategoryId
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.hours

data class Task(
    val id: TaskId,
    val title: String,
    val description: String = "",
    val categoryId: CategoryId? = null,
    val time: LocalTime,
    val minutesBefore: Int = 10,
    val taskType: TaskType = TaskType.ONE_TIME,
    val weekDays: List<DayOfWeek> = emptyList(),
    val completedDates: Set<LocalDate> = emptySet(),
    val createdAt: LocalDateTime,
    val alarmTriggeredAt: LocalDateTime? = null,
    val skipUntil: LocalDate? = null,
    val soundUri: String? = null,
) {
    init {
        require(title.isNotBlank()) { "タイトルは必須です" }
        require(title.length <= MAX_TITLE_LENGTH) { "タイトルは${MAX_TITLE_LENGTH}文字以下にしてください" }
        require(minutesBefore in 0..MAX_MINUTES_BEFORE) { "通知時間は0〜${MAX_MINUTES_BEFORE}分の範囲で指定してください" }
        if (taskType == TaskType.WEEKLY_LOOP) {
            require(weekDays.isNotEmpty()) { "週次タスクには少なくとも1つの曜日を指定してください" }
        }
    }

    companion object {
        private const val DELETION_THRESHOLD_HOURS = 24
        private const val MAX_TITLE_LENGTH = 100
        private const val MAX_MINUTES_BEFORE = 60
    }

    /**
     * 指定した日付でタスクを完了する
     *
     * @param date 完了する日付
     * @return 完了状態が更新されたタスク
     */
    fun complete(date: LocalDate): Task = copy(completedDates = completedDates + date)

    /**
     * 指定した日付の完了状態をリセットする
     *
     * @param date リセットする日付
     * @return 完了状態がリセットされたタスク
     */
    fun resetCompletion(date: LocalDate): Task = copy(completedDates = completedDates - date)

    /**
     * 全ての完了状態をリセットする（週次タスク用）
     */
    fun resetAllCompletions(): Task = copy(completedDates = emptySet(), alarmTriggeredAt = null)

    /**
     * 指定した日に完了済みかどうか
     *
     * @param date チェックする日付
     * @return 完了済みの場合はtrue
     */
    fun isCompletedOn(date: LocalDate): Boolean = completedDates.contains(date)

    fun markAlarmTriggered(triggeredAt: LocalDateTime): Task =
        copy(alarmTriggeredAt = triggeredAt)

    /**
     * 指定した日まで週次タスクをスキップする
     *
     * @param until スキップする期限日
     * @return スキップ設定されたタスク
     */
    fun skip(until: LocalDate): Task {
        require(taskType == TaskType.WEEKLY_LOOP) { "スキップは週次タスクのみ可能です" }
        return copy(skipUntil = until)
    }

    fun cancelSkip(): Task = copy(skipUntil = null)

    /**
     * 現在スキップ中かどうか
     *
     * @param today 今日の日付
     * @return スキップ中の場合はtrue
     */
    fun isSkipped(today: LocalDate): Boolean {
        val until = skipUntil ?: return false
        return today <= until
    }

    /**
     * 指定した日にアクティブかどうか
     *
     * @param date チェックする日付
     * @return アクティブな場合はtrue
     */
    fun isActiveOn(date: LocalDate): Boolean {
        // その日に完了済みならアクティブでない
        if (isCompletedOn(date)) return false

        // スキップ中はアクティブでない
        if (isSkipped(date)) return false

        return when (taskType) {
            TaskType.ONE_TIME -> {
                // 1回限りのタスクは作成日と同じ日のみアクティブ
                createdAt.date == date
            }
            TaskType.WEEKLY_LOOP -> {
                // 週次タスクは指定した曜日にアクティブ
                weekDays.contains(date.dayOfWeek)
            }
        }
    }

    /**
     * 24時間経過後に削除対象かどうか
     *
     * @param now 現在日時
     * @param timeZone タイムゾーン
     * @return 削除すべき場合はtrue
     */
    fun shouldBeDeleted(now: LocalDateTime, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        // WEEKLY_LOOP タスクは削除しない
        if (taskType == TaskType.WEEKLY_LOOP) return false

        // アラームが発火していない場合は削除しない
        val triggered = alarmTriggeredAt ?: return false

        // 24時間経過しているかチェック
        val triggeredInstant = triggered.toInstant(timeZone)
        val nowInstant = now.toInstant(timeZone)
        val elapsed = nowInstant - triggeredInstant
        
        return elapsed >= DELETION_THRESHOLD_HOURS.hours
    }
}
