package com.segnities007.yatte.presentation.feature.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.core.component.FloatingHeaderBar
import com.segnities007.yatte.presentation.core.component.FloatingHeaderBarDefaults

import kotlinx.datetime.DayOfWeek
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.*
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.task.generated.resources.*
import yatte.presentation.feature.task.generated.resources.Res as TaskRes
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.feature.task.component.TaskTimePickerSheet
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun TaskFormScreen(
    taskId: String? = null,
    viewModel: TaskFormViewModel = koinViewModel(),
    actions: TaskActions,
    isNavigationVisible: Boolean = true, // Default true if not provided (though always provided by nav)
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(taskId) {
        taskId?.let { viewModel.loadTask(it) }
    }

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

    // FloatingHeaderBarの高さのみ考慮（ContainerHeight + TopMargin）
    val headerHeight = FloatingHeaderBarDefaults.ContainerHeight + FloatingHeaderBarDefaults.TopMargin

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ヘッダー分のスペースを確保（スクロール時にコンテンツがヘッダー下を通過可能）
            Spacer(modifier = Modifier.height(headerHeight))
            // タイトル
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onIntent(TaskFormIntent.UpdateTitle(it)) },
                label = { Text(stringResource(TaskRes.string.field_task_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // 時間指定
            Text(
                text = stringResource(TaskRes.string.label_execute_time),
                style = MaterialTheme.typography.labelLarge,
            )
            var showTimeSheet by remember { mutableStateOf(false) }

            // Time Display Button (Triggers Sheet)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .bounceClick()
                    .clickable { showTimeSheet = true }
                // Removed padding(vertical=12.dp) from here to apply it inside Surface or ensure Surface covers it
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer, // Distinct background color
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Better visual representation
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "${state.time.hour.toString().padStart(2, '0')}:${
                                state.time.minute.toString().padStart(2, '0')
                            }",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "タップして時間を変更",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            if (showTimeSheet) {
                TaskTimePickerSheet(
                    initialTime = state.time,
                    onDismiss = { showTimeSheet = false },
                    onConfirm = { newTime ->
                        viewModel.onIntent(TaskFormIntent.UpdateTime(newTime))
                        showTimeSheet = false
                    }
                )
            }

            // タスクタイプ
            Text(
                text = stringResource(TaskRes.string.label_task_type),
                style = MaterialTheme.typography.labelLarge,
            )
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                TaskType.entries.forEachIndexed { index, type ->
                    SegmentedButton(
                        selected = state.taskType == type,
                        onClick = { viewModel.onIntent(TaskFormIntent.UpdateTaskType(type)) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = TaskType.entries.size,
                        ),
                        modifier = Modifier.bounceClick()
                    ) {
                        Text(
                            if (type == TaskType.ONE_TIME) {
                                stringResource(TaskRes.string.task_type_one_time)
                            } else {
                                stringResource(TaskRes.string.task_type_weekly)
                            }
                        )
                    }
                }
            }

            // 週次の場合: 曜日選択
            if (state.taskType == TaskType.WEEKLY_LOOP) {
                Text(
                    text = stringResource(TaskRes.string.label_repeat_weekdays),
                    style = MaterialTheme.typography.labelLarge,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DayOfWeek.entries.forEach { day ->
                        FilterChip(
                            selected = day in state.selectedWeekDays,
                            onClick = { viewModel.onIntent(TaskFormIntent.ToggleWeekDay(day)) },
                            label = { Text(day.toDisplayString()) },
                            modifier = Modifier.bounceClick()
                        )
                    }
                }
            }

            // 通知時間（何分前）
            Text(
                text = stringResource(TaskRes.string.notification_minutes_before, state.minutesBefore),
                style = MaterialTheme.typography.labelLarge,
            )
            Slider(
                value = state.minutesBefore.toFloat(),
                onValueChange = { viewModel.onIntent(TaskFormIntent.UpdateMinutesBefore(it.toInt())) },
                valueRange = 0f..60f,
                steps = 11,
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        FloatingHeaderBar(
            isVisible = isNavigationVisible,
            title = {
                Text(
                    if (state.isEditMode) {
                        stringResource(TaskRes.string.title_edit_task)
                    } else {
                        stringResource(TaskRes.string.title_add_task)
                    }
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.onIntent(TaskFormIntent.Cancel) },
                    modifier = Modifier.bounceClick()
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(CoreRes.string.common_back),
                    )
                }
            },
            actions = {
                if (state.isEditMode) {
                    IconButton(
                        onClick = { viewModel.onIntent(TaskFormIntent.DeleteTask) },
                        modifier = Modifier.bounceClick()
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(CoreRes.string.common_delete),
                        )
                    }
                }
                IconButton(
                    onClick = { viewModel.onIntent(TaskFormIntent.SaveTask) },
                    modifier = Modifier.bounceClick()
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = stringResource(CoreRes.string.common_save),
                    )
                }
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun DayOfWeek.toDisplayString(): String = when (this) {
    DayOfWeek.MONDAY -> stringResource(TaskRes.string.weekday_mon_short)
    DayOfWeek.TUESDAY -> stringResource(TaskRes.string.weekday_tue_short)
    DayOfWeek.WEDNESDAY -> stringResource(TaskRes.string.weekday_wed_short)
    DayOfWeek.THURSDAY -> stringResource(TaskRes.string.weekday_thu_short)
    DayOfWeek.FRIDAY -> stringResource(TaskRes.string.weekday_fri_short)
    DayOfWeek.SATURDAY -> stringResource(TaskRes.string.weekday_sat_short)
    DayOfWeek.SUNDAY -> stringResource(TaskRes.string.weekday_sun_short)
}
