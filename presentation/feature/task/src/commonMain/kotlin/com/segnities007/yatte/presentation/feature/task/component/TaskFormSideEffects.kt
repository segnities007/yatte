package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.segnities007.yatte.presentation.feature.task.TaskActions
import com.segnities007.yatte.presentation.feature.task.TaskFormEvent
import com.segnities007.yatte.presentation.feature.task.TaskFormViewModel
import org.jetbrains.compose.resources.getString
import yatte.presentation.feature.task.generated.resources.snackbar_task_deleted
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

@Composable
fun TaskFormSideEffects(
    viewModel: TaskFormViewModel,
    actions: TaskActions,
    onShowSnackbar: (String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TaskFormEvent.TaskSaved -> actions.onBack()
                is TaskFormEvent.TaskDeleted -> {
                    onShowSnackbar(getString(TaskRes.string.snackbar_task_deleted))
                    actions.onBack()
                }
                is TaskFormEvent.Cancelled -> actions.onBack()
                is TaskFormEvent.ShowError -> onShowSnackbar(event.message)
            }
        }
    }
}
