package com.segnities007.yatte.presentation.feature.management

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteScaffold
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.cd_add_task
import yatte.presentation.core.generated.resources.nav_manage
import yatte.presentation.core.generated.resources.Res as CoreRes

import com.segnities007.yatte.presentation.feature.management.component.TaskManagementContent
import com.segnities007.yatte.presentation.feature.management.component.TaskManagementSetupHeader
import com.segnities007.yatte.presentation.feature.management.component.TaskManagementSetupSideEffects

@Composable
fun TaskManagementScreen(
    viewModel: TaskManagementViewModel = koinViewModel(),
    actions: TaskManagementActions,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isNavigationVisible: Boolean,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TaskManagementSetupHeader(actions = actions)
    TaskManagementSetupSideEffects(
        viewModel = viewModel,
        actions = actions,
        onShowSnackbar = onShowSnackbar
    )

    TaskManagementScreen(
        state = state,
        contentPadding = contentPadding,
        isNavigationVisible = isNavigationVisible,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun TaskManagementScreen(
    state: TaskManagementState,
    contentPadding: PaddingValues,
    isNavigationVisible: Boolean,
    onIntent: (TaskManagementIntent) -> Unit,
) {
    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        TaskManagementContent(
            state = state,
            onIntent = onIntent,
            contentPadding = listContentPadding,
        )
    }
}



@Composable
@Preview
fun TaskManagementScreenPreview() {
    MaterialTheme {
        TaskManagementScreen(
            state = TaskManagementState(),
            contentPadding = PaddingValues(0.dp),
            isNavigationVisible = true,
            onIntent = {},
        )
    }
}
