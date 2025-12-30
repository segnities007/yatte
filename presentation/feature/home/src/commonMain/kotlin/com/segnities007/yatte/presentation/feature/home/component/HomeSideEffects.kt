package com.segnities007.yatte.presentation.feature.home.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.segnities007.yatte.presentation.feature.home.HomeActions
import com.segnities007.yatte.presentation.feature.home.HomeEvent
import com.segnities007.yatte.presentation.feature.home.HomeIntent
import com.segnities007.yatte.presentation.feature.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.home.generated.resources.snackbar_task_completed
import yatte.presentation.feature.home.generated.resources.snackbar_undo
import yatte.presentation.feature.home.generated.resources.Res as HomeRes

@Composable
fun HomeSideEffects(
    viewModel: HomeViewModel,
    actions: HomeActions,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: (String) -> Unit,
) {
    val undoLabel = stringResource(HomeRes.string.snackbar_undo)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToAddTask -> actions.onAddTask()
                is HomeEvent.NavigateToHistory -> actions.onHistory()
                is HomeEvent.NavigateToSettings -> actions.onSettings()
                is HomeEvent.NavigateToEditTask -> actions.onEditTask(event.taskId)
                is HomeEvent.ShowError -> onShowSnackbar(event.message)
                is HomeEvent.ShowTaskCompleted -> {
                    val message = getString(HomeRes.string.snackbar_task_completed, event.task.title)
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = undoLabel,
                            duration = SnackbarDuration.Short,
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onIntent(HomeIntent.UndoCompleteTask(event.task, event.date))
                        }
                    }
                }
            }
        }
    }
}
