package com.segnities007.yatte

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.mikepenz.aboutlibraries.Libs
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import com.segnities007.yatte.domain.aggregate.settings.usecase.GetSettingsUseCase
import com.segnities007.yatte.presentation.core.component.LocalLibraries
import com.segnities007.yatte.presentation.core.theme.YatteTheme
import com.segnities007.yatte.presentation.navigation.AppNavHost
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import yatte.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun App() {
    val getSettingsUseCase: GetSettingsUseCase = koinInject()
    val settings by getSettingsUseCase().collectAsState(initial = null)
    val themeMode = settings?.themeMode ?: ThemeMode.SYSTEM

    YatteTheme(themeMode = themeMode) {
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

        val libraries by produceState<Libs?>(null) {
            value =
                try {
                    val bytes = Res.readBytes("files/aboutlibraries.json")
                    Libs.Builder().withJson(bytes.decodeToString()).build()
                } catch (e: Exception) {
                    null
                }
        }

        CompositionLocalProvider(LocalLibraries provides libraries) {
            AppNavHost(
                navController = navController,
                snackbarHostState = snackbarHostState,
                onShowSnackbar = showSnackbar,
            )
        }
    }
}
