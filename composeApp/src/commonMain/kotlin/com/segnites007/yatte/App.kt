package com.segnites007.yatte

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.feature.history.HistoryScreen
import com.segnities007.yatte.presentation.feature.home.HomeScreen
import com.segnities007.yatte.presentation.feature.settings.SettingsScreen
import com.segnities007.yatte.presentation.feature.task.TaskFormScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun App() {
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var currentRoute by remember { mutableStateOf<Route>(Route.Home) }
        var editingTaskId by remember { mutableStateOf<String?>(null) }

        val showSnackbar: (String) -> Unit = { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            when (currentRoute) {
                is Route.Home -> {
                    HomeScreen(
                        onNavigateToAddTask = { currentRoute = Route.AddTask },
                        onNavigateToHistory = { currentRoute = Route.History },
                        onNavigateToSettings = { currentRoute = Route.Settings },
                        onShowSnackbar = showSnackbar,
                    )
                }
                is Route.AddTask -> {
                    TaskFormScreen(
                        taskId = null,
                        onNavigateBack = { currentRoute = Route.Home },
                        onShowSnackbar = showSnackbar,
                    )
                }
                is Route.EditTask -> {
                    TaskFormScreen(
                        taskId = editingTaskId,
                        onNavigateBack = { currentRoute = Route.Home },
                        onShowSnackbar = showSnackbar,
                    )
                }
                is Route.History -> {
                    HistoryScreen(
                        onNavigateBack = { currentRoute = Route.Home },
                        onShowSnackbar = showSnackbar,
                    )
                }
                is Route.Settings -> {
                    SettingsScreen(
                        onNavigateBack = { currentRoute = Route.Home },
                        onShowSnackbar = showSnackbar,
                    )
                }
            }
        }
    }
}
