package com.segnities007.yatte.presentation.feature.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import kotlinx.datetime.DayOfWeek
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TaskFormScreen(
    taskId: String? = null,
    viewModel: TaskFormViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(taskId) {
        taskId?.let { viewModel.loadTask(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TaskFormEvent.TaskSaved -> onNavigateBack()
                is TaskFormEvent.Cancelled -> onNavigateBack()
                is TaskFormEvent.ShowError -> onShowSnackbar(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditMode) "タスク編集" else "タスク追加") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(TaskFormIntent.Cancel) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onIntent(TaskFormIntent.SaveTask) }) {
                        Icon(Icons.Default.Check, contentDescription = "保存")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // タイトル
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onIntent(TaskFormIntent.UpdateTitle(it)) },
                label = { Text("タスク名") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // タスクタイプ
            Text(
                text = "タスクタイプ",
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
                        Text(if (type == TaskType.ONE_TIME) "単発" else "週次繰り返し")
                    }
                }
            }

            // 週次の場合: 曜日選択
            if (state.taskType == TaskType.WEEKLY_LOOP) {
                Text(
                    text = "繰り返す曜日",
                    style = MaterialTheme.typography.labelLarge,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DayOfWeek.entries.forEach { day ->
                        FilterChip(
                            selected = day in state.selectedWeekDays,
                            onClick = { viewModel.onIntent(TaskFormIntent.ToggleWeekDay(day)) },
                            label = { Text(day.toJapanese()) },
                        )
                    }
                }
            }

            // 通知時間（何分前）
            Text(
                text = "通知: ${state.minutesBefore}分前",
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
    }
}

private fun DayOfWeek.toJapanese(): String = when (this) {
    DayOfWeek.MONDAY -> "月"
    DayOfWeek.TUESDAY -> "火"
    DayOfWeek.WEDNESDAY -> "水"
    DayOfWeek.THURSDAY -> "木"
    DayOfWeek.FRIDAY -> "金"
    DayOfWeek.SATURDAY -> "土"
    DayOfWeek.SUNDAY -> "日"
    else -> ""
}
