package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.presentation.designsystem.component.button.YatteFilledIconButton
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.theme.YatteBrushes
import com.segnities007.yatte.presentation.feature.task.TaskFormIntent
import com.segnities007.yatte.presentation.feature.task.TaskFormState
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.common_back
import yatte.presentation.core.generated.resources.common_delete
import yatte.presentation.core.generated.resources.common_save
import yatte.presentation.feature.task.generated.resources.title_add_task
import yatte.presentation.feature.task.generated.resources.title_edit_task
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

@Composable
fun TaskFormHeader(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
) {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val isEditMode = state.isEditMode
    val titleAddTask = stringResource(TaskRes.string.title_add_task)
    val titleEditTask = stringResource(TaskRes.string.title_edit_task)
    val backDesc = stringResource(CoreRes.string.common_back)
    val deleteDesc = stringResource(CoreRes.string.common_delete)
    val saveDesc = stringResource(CoreRes.string.common_save)
    
    val headerConfig = remember(isEditMode) {
        HeaderConfig(
            title = {
                Text(if (isEditMode) titleEditTask else titleAddTask)
            },
            navigationIcon = {
                YatteIconButton(
                    onClick = { onIntent(TaskFormIntent.Cancel) },
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = backDesc,
                )
            },
            actions = {
                if (isEditMode) {
                    YatteIconButton(
                        onClick = { onIntent(TaskFormIntent.DeleteTask) },
                        icon = Icons.Default.Delete,
                        contentDescription = deleteDesc,
                    )
                }
                YatteIconButton(
                    onClick = { onIntent(TaskFormIntent.SaveTask) },
                    icon = Icons.Default.Check,
                    contentDescription = saveDesc,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
        )
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}
