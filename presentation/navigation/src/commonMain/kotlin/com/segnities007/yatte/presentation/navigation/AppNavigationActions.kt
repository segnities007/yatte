package com.segnities007.yatte.presentation.navigation

import androidx.navigation.NavController
import com.segnities007.yatte.presentation.feature.history.HistoryActions
import com.segnities007.yatte.presentation.feature.history.HistoryRoute
import com.segnities007.yatte.presentation.feature.task.AddTaskRoute
import com.segnities007.yatte.presentation.feature.task.EditTaskRoute
import com.segnities007.yatte.presentation.feature.home.HomeActions
import com.segnities007.yatte.presentation.feature.settings.SettingsActions
import com.segnities007.yatte.presentation.feature.settings.SettingsRoute
import com.segnities007.yatte.presentation.feature.task.TaskActions
import com.segnities007.yatte.presentation.feature.management.TaskManagementActions

/**
 * アプリ全体のナビゲーションアクションを管理するクラス。
 *
 * 仕様:
 * - 各機能モジュール（Home, Task, History, Settings）への遷移ロジックを集約するコンテナとして機能する
 * - `NavController` をラップし、画面遷移の実装詳細を `AppNavHost` から隠蔽する
 * - 各画面には `HomeActions` などの粒度でアクションを注入する
 *
 * 前提:
 * - `AppNavHost` 内で `remember(navController)` を使用してインスタンス化すること
 */
class AppNavigationActions(private val navController: NavController) {
    val homeActions: HomeActions = HomeActions(
        onAddTask = { navController.navigate(AddTaskRoute) },
        onHistory = { navController.navigate(HistoryRoute) },
        onSettings = { navController.navigate(SettingsRoute) },
        onEditTask = { taskId -> navController.navigate(EditTaskRoute(taskId)) },
    )
    val taskManagementActions: TaskManagementActions = TaskManagementActions(
        onAddTask = { navController.navigate(AddTaskRoute) },
        onEditTask = { taskId -> navController.navigate(EditTaskRoute(taskId)) },
    )
    val taskActions: TaskActions = TaskActions(
        onBack = { navController.popBackStack() },
    )
    val historyActions: HistoryActions = HistoryActions(
        onBack = { navController.popBackStack() },
    )
    val settingsActions: SettingsActions = SettingsActions(
        onBack = { navController.popBackStack() },
    )
}
