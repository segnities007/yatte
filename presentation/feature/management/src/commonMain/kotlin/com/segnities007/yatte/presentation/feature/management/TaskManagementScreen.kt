package com.segnities007.yatte.presentation.feature.management

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.presentation.core.component.FloatingHeaderBar
import com.segnities007.yatte.presentation.core.component.FloatingHeaderBarDefaults
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.feature.management.generated.resources.*
import yatte.presentation.feature.management.generated.resources.Res as ManagementRes
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.core.generated.resources.nav_manage
import yatte.presentation.core.generated.resources.cd_add_task

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

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val headerHeight = FloatingHeaderBarDefaults.ContainerHeight + FloatingHeaderBarDefaults.TopMargin + FloatingHeaderBarDefaults.BottomSpacing
    val listContentPadding = PaddingValues(
        top = statusBarHeight + headerHeight,
        bottom = contentPadding.calculateBottomPadding(),
        start = 16.dp,
        end = 16.dp,
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TaskManagementList(
            tasks = state.tasks,
            contentPadding = listContentPadding,
            onTaskClick = { viewModel.onIntent(TaskManagementIntent.NavigateToEditTask(it.id.value)) },
            modifier = Modifier.fillMaxSize(),
        )

        FloatingHeaderBar(
            title = { Text(stringResource(CoreRes.string.nav_manage)) },
            isVisible = isNavigationVisible,
            actions = {
                IconButton(onClick = actions.onAddTask) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(CoreRes.string.cd_add_task),
                    )
                }
            },
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
