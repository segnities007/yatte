package com.segnities007.yatte.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.segnities007.yatte.presentation.feature.history.HistoryScreen
import com.segnities007.yatte.presentation.feature.home.HomeScreen
import com.segnities007.yatte.presentation.feature.settings.SettingsScreen
import com.segnities007.yatte.presentation.feature.task.TaskFormScreen

/**
 * アプリのメインナビゲーションホスト
 * JetBrains Navigation Compose を使用した型安全なナビゲーション
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onShowSnackbar: (String) -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToAddTask = { navController.navigate(AddTaskRoute) },
                onNavigateToHistory = { navController.navigate(HistoryRoute) },
                onNavigateToSettings = { navController.navigate(SettingsRoute) },
                onShowSnackbar = onShowSnackbar,
            )
        }

        composable<AddTaskRoute> {
            TaskFormScreen(
                taskId = null,
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
            )
        }

        composable<EditTaskRoute> { backStackEntry ->
            val route: EditTaskRoute = backStackEntry.toRoute()
            TaskFormScreen(
                taskId = route.taskId,
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
            )
        }

        composable<HistoryRoute> {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}
