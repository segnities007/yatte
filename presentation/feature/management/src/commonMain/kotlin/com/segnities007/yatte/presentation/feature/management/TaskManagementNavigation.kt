package com.segnities007.yatte.presentation.feature.management

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TaskManagementRoute

data class TaskManagementActions(
    val onAddTask: () -> Unit,
    val onEditTask: (String) -> Unit,
)

fun NavGraphBuilder.taskManagementScreen(
    actions: TaskManagementActions,
    contentPadding: PaddingValues,
    isNavigationVisible: Boolean,
    onShowSnackbar: (String) -> Unit,
) {
    composable<TaskManagementRoute> {
        TaskManagementScreen(
            actions = actions,
            contentPadding = contentPadding,
            isNavigationVisible = isNavigationVisible,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
