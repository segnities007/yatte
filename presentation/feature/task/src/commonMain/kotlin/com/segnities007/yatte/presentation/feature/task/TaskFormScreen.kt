package com.segnities007.yatte.presentation.feature.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteScaffold
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.common_back
import yatte.presentation.core.generated.resources.common_delete
import yatte.presentation.core.generated.resources.common_save
import yatte.presentation.feature.task.generated.resources.snackbar_task_deleted
import yatte.presentation.feature.task.generated.resources.title_add_task
import yatte.presentation.feature.task.generated.resources.title_edit_task
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

import com.segnities007.yatte.presentation.feature.task.component.TaskFormContent
import com.segnities007.yatte.presentation.feature.task.component.TaskFormHeader
import com.segnities007.yatte.presentation.feature.task.component.TaskFormSideEffects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskFormScreen(
    taskId: String? = null,
    viewModel: TaskFormViewModel = koinViewModel(),
    actions: TaskActions,
    isNavigationVisible: Boolean = true,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val soundPickerLauncher = rememberSoundPickerLauncher(
        onResult = { uri ->
            if (uri != null) {
                viewModel.onIntent(TaskFormIntent.UpdateSoundUri(uri))
            }
        }
    )

    LaunchedEffect(taskId) {
        taskId?.let { viewModel.loadTask(it) }
    }

    TaskFormHeader(state = state, onIntent = viewModel::onIntent)
    TaskFormSideEffects(viewModel = viewModel, actions = actions, onShowSnackbar = onShowSnackbar)

    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = PaddingValues(0.dp),
    ) { listContentPadding ->
        TaskFormContent(
            state = state,
            onIntent = viewModel::onIntent,
            soundPickerLauncher = soundPickerLauncher,
            contentPadding = listContentPadding,
        )
    }
}


