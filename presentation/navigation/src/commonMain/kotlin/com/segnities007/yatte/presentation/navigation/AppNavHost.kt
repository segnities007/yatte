package com.segnities007.yatte.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.segnities007.yatte.presentation.core.component.AppBottomBar
import com.segnities007.yatte.presentation.core.component.AppFloatingActionButton
import com.segnities007.yatte.presentation.core.component.NavItem
import com.segnities007.yatte.presentation.feature.history.HistoryRoute
import com.segnities007.yatte.presentation.feature.history.historyScreen
import com.segnities007.yatte.presentation.feature.home.HomeRoute
import com.segnities007.yatte.presentation.feature.home.homeScreen
import com.segnities007.yatte.presentation.feature.settings.SettingsRoute
import com.segnities007.yatte.presentation.feature.settings.settingsScreen
import com.segnities007.yatte.presentation.feature.task.AddTaskRoute
import com.segnities007.yatte.presentation.feature.task.EditTaskRoute
import com.segnities007.yatte.presentation.feature.task.taskScreens
import com.segnities007.yatte.presentation.feature.management.TaskManagementRoute
import com.segnities007.yatte.presentation.feature.management.taskManagementScreen

/**
 * アプリのメインナビゲーションホスト。
 *
 * 仕様:
 * - [AppNavigationActions] を使用してナビゲーションロジックを管理する
 * - BottomBar と FAB の表示制御を行う（現在のルートに基づく）
 * - 各機能モジュールの画面定義（[homeScreen] 等）を呼び出す
 */
@Composable
fun AppNavHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onShowSnackbar: (String) -> Unit = {},
) {
    var showNavBar by remember { mutableStateOf(true) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val actions = remember(navController) { AppNavigationActions(navController) }

    // ナビゲーションバーを表示するかどうか
    showNavBar = when {
        currentRoute?.contains("AddTaskRoute") == true -> false
        currentRoute?.contains("EditTaskRoute") == true -> false
        else -> true
    }

    val currentNavItem = when {
        currentRoute?.contains("HistoryRoute") == true -> NavItem.HISTORY
        currentRoute?.contains("TaskManagementRoute") == true -> NavItem.MANAGE
        currentRoute?.contains("SettingsRoute") == true -> NavItem.SETTINGS
        else -> NavItem.HOME
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppBottomBar(
                isVisible = showNavBar,
                currentItem = currentNavItem,
                onItemSelected = { item ->
                    when (item) {
                        NavItem.HOME -> navController.navigate(HomeRoute) {
                            popUpTo(HomeRoute) { inclusive = true }
                        }
                        NavItem.MANAGE -> navController.navigate(TaskManagementRoute) {
                            popUpTo(HomeRoute) { inclusive = false }
                        }
                        NavItem.HISTORY -> navController.navigate(HistoryRoute) {
                            popUpTo(HomeRoute) { inclusive = false }
                        }
                        NavItem.SETTINGS -> navController.navigate(SettingsRoute) {
                            popUpTo(HomeRoute) { inclusive = false }
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                isVisible = showNavBar,
                onClick = actions.homeActions.onAddTask,
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(padding),
        ) {
            homeScreen(
                actions = actions.homeActions,
                onShowSnackbar = onShowSnackbar,
            )

            taskManagementScreen(
                actions = actions.taskManagementActions,
                onShowSnackbar = onShowSnackbar,
            )

            taskScreens(
                actions = actions.taskActions,
                onShowSnackbar = onShowSnackbar,
            )

            historyScreen(
                actions = actions.historyActions,
                onShowSnackbar = onShowSnackbar,
            )

            settingsScreen(
                actions = actions.settingsActions,
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}
