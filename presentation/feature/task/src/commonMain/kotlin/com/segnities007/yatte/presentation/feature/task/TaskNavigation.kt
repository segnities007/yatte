package com.segnities007.yatte.presentation.feature.task

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object AddTaskRoute

@Serializable
data class EditTaskRoute(val taskId: String)

/**
 * Task編集/作成画面から実行可能なナビゲーションアクションの定義。
 */
data class TaskActions(
    val onBack: () -> Unit,
)

/**
 * Task機能（作成・編集）に関連するナビゲーショングラフ。
 *
 * 仕様:
 * - [AddTaskRoute]（新規作成）と [EditTaskRoute]（編集）の2つの画面を含む
 */
fun NavGraphBuilder.taskScreens(
    actions: TaskActions,
    onShowSnackbar: (String) -> Unit,
) {
    composable<AddTaskRoute> {
        TaskFormScreen(
            taskId = null,
            actions = actions,
            onShowSnackbar = onShowSnackbar,
        )
    }

    composable<EditTaskRoute> { backStackEntry ->
        val route: EditTaskRoute = backStackEntry.toRoute()
        TaskFormScreen(
            taskId = route.taskId,
            actions = actions,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
