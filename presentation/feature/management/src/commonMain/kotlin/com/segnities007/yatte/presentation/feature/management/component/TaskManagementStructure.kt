package com.segnities007.yatte.presentation.feature.management.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.feature.management.TaskManagementActions
import com.segnities007.yatte.presentation.feature.management.TaskManagementEvent
import com.segnities007.yatte.presentation.feature.management.TaskManagementIntent
import com.segnities007.yatte.presentation.feature.management.TaskManagementState
import com.segnities007.yatte.presentation.feature.management.TaskManagementViewModel
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.cd_add_task
import yatte.presentation.core.generated.resources.nav_manage
import yatte.presentation.core.generated.resources.Res as CoreRes

@Composable
fun TaskManagementSetupHeader(
    actions: TaskManagementActions,
) {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val manageTitle = stringResource(CoreRes.string.nav_manage)
    val addTaskDesc = stringResource(CoreRes.string.cd_add_task)
    
    val headerConfig = remember {
        HeaderConfig(
            title = { Text(manageTitle) },
            actions = {
                YatteIconButton(
                    onClick = actions.onAddTask,
                    icon = Icons.Default.Add,
                    contentDescription = addTaskDesc,
                )
            },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}

@Composable
fun TaskManagementSetupSideEffects(
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
fun TaskManagementContent(
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
