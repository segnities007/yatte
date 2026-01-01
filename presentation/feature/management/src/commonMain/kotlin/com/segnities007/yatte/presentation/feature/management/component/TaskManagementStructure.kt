package com.segnities007.yatte.presentation.feature.management.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.unit.dp
import kotlin.time.Clock
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import com.segnities007.yatte.domain.aggregate.task.model.Task
import com.segnities007.yatte.domain.aggregate.task.model.TaskId
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme

@Composable
fun TaskManagementSetupHeader(
    actions: TaskManagementActions,
) {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val manageTitle = stringResource(CoreRes.string.nav_manage)
    val addTaskDesc = stringResource(CoreRes.string.cd_add_task)
    
    val headerConfig = remember {
        HeaderConfig(
            title = { YatteText(manageTitle) },
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

@Composable
@Preview
fun TaskManagementContentPreview() {
    YatteTheme {
        TaskManagementContent(
            state = TaskManagementState(
                tasks = listOf(
                    Task(
                        id = TaskId("1"),
                        title = "Weekly Meeting",
                        time = LocalTime(14, 0),
                        createdAt = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()).toLocalDateTime(TimeZone.currentSystemDefault()),
                    )
                )
            ),
            onIntent = {},
            contentPadding = PaddingValues(0.dp),
        )
    }
}
