package com.segnities007.yatte.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
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
import com.segnities007.yatte.presentation.feature.management.TaskManagementRoute
import com.segnities007.yatte.presentation.feature.management.taskManagementScreen
import com.segnities007.yatte.presentation.feature.settings.SettingsRoute
import com.segnities007.yatte.presentation.feature.settings.settingsScreen
import com.segnities007.yatte.presentation.feature.task.taskScreens
import com.segnities007.yatte.presentation.designsystem.effect.ConfettiHost


/**
 * アプリのメインナビゲーションホスト。
 *
 * 仕様:
 * - Overlay Navigation: ScaffoldのbottomBarを使用せず、Boxでコンテンツの上にFNBを配置
 * - FNB Floating Effect: 下部に余白を設け、背景を透過させて浮遊感を演出
 * - Header Fix: ScaffoldのcontentWindowInsetsを0にし、各画面での二重適用を防ぐ
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
        else -> true
    }

    val currentNavItem = when {
        currentRoute?.contains("HistoryRoute") == true -> NavItem.HISTORY
        currentRoute?.contains("TaskManagementRoute") == true -> NavItem.MANAGE
        currentRoute?.contains("SettingsRoute") == true -> NavItem.SETTINGS
        else -> NavItem.HOME
    }

    // スクロールによる表示制御
    var isFnbVisible by remember { mutableStateOf(true) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 下スクロール（available.y < 0）で非表示、上スクロール（available.y > 0）で表示
                if (available.y < -5f) { // 閾値を設けて感度調整
                    isFnbVisible = false
                } else if (available.y > 5f) {
                    isFnbVisible = true
                }
                return Offset.Zero
            }
        }
    }

    // ルートによる表示制御とスクロールによる表示制御を合成
    // 特定のルート（詳細画面など）では常に非表示にする場合はここで制御
    // タスク追加/編集画面かどうかを判定
    val isTaskFormScreen = currentRoute?.contains("AddTaskRoute") == true ||
            currentRoute?.contains("EditTaskRoute") == true

    // タスクフォーム画面ではBottomBarを非表示
    val isBottomBarVisible = showNavBar && isFnbVisible && !isTaskFormScreen

    val showFab = currentNavItem == NavItem.HOME && isBottomBarVisible && !isTaskFormScreen

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection),
            contentWindowInsets = WindowInsets(0),
            // SnackbarHost は Box overlay に移動してFNBの上に配置
        ) { contentPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier.fillMaxSize(),
            ) {
                homeScreen(
                    actions = actions.homeActions,
                    contentPadding = contentPadding,
                    isNavigationVisible = isBottomBarVisible,
                    onShowSnackbar = onShowSnackbar,
                )

                taskManagementScreen(
                    actions = actions.taskManagementActions,
                    contentPadding = contentPadding,
                    isNavigationVisible = isBottomBarVisible,
                    onShowSnackbar = onShowSnackbar,
                )

                taskScreens(
                    actions = actions.taskActions,
                    isNavigationVisible = isFnbVisible,
                    onShowSnackbar = onShowSnackbar,
                )

                historyScreen(
                    actions = actions.historyActions,
                    contentPadding = contentPadding,
                    isNavigationVisible = isBottomBarVisible,
                    onShowSnackbar = onShowSnackbar,
                )

                settingsScreen(
                    actions = actions.settingsActions,
                    contentPadding = contentPadding,
                    isNavigationVisible = isBottomBarVisible,
                    onShowSnackbar = onShowSnackbar,
                )
            }
        }

        // FNB Overlay
        AppBottomBar(
            isVisible = isBottomBarVisible,
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
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp), // Increased from 16.dp for better reachability
        )

        // FAB Overlay
        AppFloatingActionButton(
            isVisible = showFab,
            onClick = actions.homeActions.onAddTask,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                // FNBの上部に配置 (112dp + margin) -> adjusted to match new BottomBar padding
                .padding(bottom = 136.dp, end = 24.dp),
        )
        
        // Snackbar Overlay (FNBの上に表示)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 140.dp), // FNB(112dp) + margin より上に配置
        )
        
        // Confetti Overlay
        ConfettiHost()
    }
}
