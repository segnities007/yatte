package com.segnities007.yatte.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * ホーム画面（今日のタスク一覧）
 */
@Serializable
data object HomeRoute

/**
 * タスク追加画面
 */
@Serializable
data object AddTaskRoute

/**
 * タスク編集画面
 */
@Serializable
data class EditTaskRoute(
    val taskId: String,
)

/**
 * 履歴画面
 */
@Serializable
data object HistoryRoute

/**
 * 設定画面
 */
@Serializable
data object SettingsRoute
