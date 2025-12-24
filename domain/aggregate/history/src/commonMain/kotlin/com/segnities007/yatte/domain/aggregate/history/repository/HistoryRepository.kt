package com.segnities007.yatte.domain.aggregate.history.repository

import com.segnities007.yatte.domain.aggregate.history.model.History
import com.segnities007.yatte.domain.aggregate.history.model.HistoryId
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * 履歴リポジトリのインターフェース
 */
interface HistoryRepository {
    /**
     * 指定した日付の履歴を取得
     */
    fun getByDate(date: LocalDate): Flow<List<History>>

    /**
     * 全履歴を取得
     */
    fun getAll(): Flow<List<History>>

    /**
     * 履歴を追加
     */
    suspend fun insert(history: History)

    /**
     * 履歴を削除
     */
    suspend fun delete(id: HistoryId)

    /**
     * 全履歴を削除
     */
    suspend fun deleteAll()
}
