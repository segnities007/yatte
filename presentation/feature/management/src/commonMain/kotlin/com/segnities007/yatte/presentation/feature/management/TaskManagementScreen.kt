package com.segnities007.yatte.presentation.feature.management

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.core.generated.resources.cd_add_task
import yatte.presentation.core.generated.resources.nav_manage

@OptIn(ExperimentalMaterial3Api::class)
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

    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = contentPadding,
    ) { listContentPadding ->
        TaskManagementContent(
            state = state,
            onIntent = viewModel::onIntent,
            contentPadding = listContentPadding,
        )
    }
}

@Composable
private fun TaskManagementSetupHeader(
    actions: TaskManagementActions,
) {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val manageTitle = stringResource(CoreRes.string.nav_manage)
    val addTaskDesc = stringResource(CoreRes.string.cd_add_task)
    
    val headerConfig = remember {
        HeaderConfig(
            title = { Text(manageTitle) },
            actions = {
                IconButton(
                    onClick = actions.onAddTask,
                    modifier = Modifier.bounceClick()
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = addTaskDesc,
                    )
                }
            },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}

@Composable
private fun TaskManagementSetupSideEffects(
    viewModel: TaskManagementViewModel,
    actions: TaskManagementActions,
    onShowSnackbar: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TaskManagementEvent.NavigateToEditTask -> actions.onEditTask(event.taskId)
                is TaskManagementEvent.NavigateToAddTask -> actions.onAddTask()
                is TaskManagementEvent.ShowError -> onShowSnackbar(event.message)
                is TaskManagementEvent.ShowMessage -> onShowSnackbar(event.message)
            }
        }
    }
}

@Composable
private fun TaskManagementContent(
    state: TaskManagementState,
    onIntent: (TaskManagementIntent) -> Unit,
    contentPadding: PaddingValues,
) {
    TaskManagementList(
        tasks = state.tasks,
        contentPadding = contentPadding,
        onTaskClick = { onIntent(TaskManagementIntent.NavigateToEditTask(it.id.value)) },
        modifier = Modifier.fillMaxSize(),
    )
}
