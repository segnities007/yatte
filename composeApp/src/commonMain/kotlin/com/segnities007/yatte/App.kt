package com.segnities007.yatte

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.segnities007.yatte.presentation.navigation.AppNavHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun App() {
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()

        val showSnackbar: (String) -> Unit = { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                )
            }
        }

        AppNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = showSnackbar,
        )
    }
}
