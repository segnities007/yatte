package com.segnities007.yatte.presentation.feature.management

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.feature.management.generated.resources.*
import yatte.presentation.feature.management.generated.resources.Res as ManagementRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagementScreen(
    viewModel: TaskManagementViewModel = koinViewModel(),
    actions: TaskManagementActions,
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

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = { Text(stringResource(ManagementRes.string.title_task_management)) },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            TaskManagementList(
                tasks = state.tasks,
                onTaskClick = { viewModel.onIntent(TaskManagementIntent.NavigateToEditTask(it.id.value)) },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
