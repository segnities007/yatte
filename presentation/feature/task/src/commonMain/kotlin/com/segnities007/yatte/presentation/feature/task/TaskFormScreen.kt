package com.segnities007.yatte.presentation.feature.task

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val headerHeight = FloatingHeaderBarDefaults.ContainerHeight + FloatingHeaderBarDefaults.TopMargin + FloatingHeaderBarDefaults.BottomSpacing
    val headerPaddingValues = PaddingValues(top = statusBarHeight + headerHeight)

    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(headerPaddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // 時間選択
                OutlinedTextField(
                    value = state.time.hour.toString().padStart(2, '0'),
                    onValueChange = { input ->
                        val hour = input.filter { it.isDigit() }.take(2).toIntOrNull() ?: 0
                        if (hour in 0..23) {
                            viewModel.onIntent(
                                TaskFormIntent.UpdateTime(
                                    kotlinx.datetime.LocalTime(hour, state.time.minute),
                                ),
                            )
                        }
                    },
                    label = { Text(stringResource(TaskRes.string.label_hour)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                Text(
                    text = ":",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 16.dp),
                )
                // 分選択
                OutlinedTextField(
                    value = state.time.minute.toString().padStart(2, '0'),
                    onValueChange = { input ->
                        val minute = input.filter { it.isDigit() }.take(2).toIntOrNull() ?: 0
                        if (minute in 0..59) {
                            viewModel.onIntent(
                                TaskFormIntent.UpdateTime(
                                    kotlinx.datetime.LocalTime(state.time.hour, minute),
                                ),
                            )
                        }
                    },
                    label = { Text(stringResource(TaskRes.string.label_minute)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
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
                ) {
                    DayOfWeek.entries.forEach { day ->
                        FilterChip(
                            selected = day in state.selectedWeekDays,
                            onClick = { viewModel.onIntent(TaskFormIntent.ToggleWeekDay(day)) },
                            label = { Text(day.toDisplayString()) },
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
                IconButton(onClick = { viewModel.onIntent(TaskFormIntent.Cancel) }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(CoreRes.string.common_back),
                    )
                }
            },
            actions = {
                if (state.isEditMode) {
                    IconButton(onClick = { viewModel.onIntent(TaskFormIntent.DeleteTask) }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(CoreRes.string.common_delete),
                        )
                    }
                }
                IconButton(onClick = { viewModel.onIntent(TaskFormIntent.SaveTask) }) {
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
