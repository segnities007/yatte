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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.core.component.YatteScaffold

import kotlinx.datetime.DayOfWeek
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.*
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.task.generated.resources.*
import yatte.presentation.feature.task.generated.resources.Res as TaskRes
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.YatteTextField
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.task.component.TaskTimePickerSheet
import com.segnities007.yatte.presentation.core.component.SoundPicker
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
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

    // グローバルHeaderの設定（SideEffectで即座に更新）
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
                    onClick = { viewModel.onIntent(TaskFormIntent.Cancel) },
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = backDesc,
                )
            },
            actions = {
                if (isEditMode) {
                    YatteIconButton(
                        onClick = { viewModel.onIntent(TaskFormIntent.DeleteTask) },
                        icon = Icons.Default.Delete,
                        contentDescription = deleteDesc,
                    )
                }
                YatteIconButton(
                    onClick = { viewModel.onIntent(TaskFormIntent.SaveTask) },
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

    // YatteScaffold を使用（Headerはグローバルなので省略）
    // TaskFormScreen は BottomNavigation が非表示のため contentPadding = 0
    YatteScaffold(
        isNavigationVisible = isNavigationVisible,
        contentPadding = PaddingValues(0.dp),
    ) { listContentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(listContentPadding),
            verticalArrangement = Arrangement.spacedBy(YatteSpacing.md),
        ) {
            // タイトル
            YatteTextField(
                value = state.title,
                onValueChange = { viewModel.onIntent(TaskFormIntent.UpdateTitle(it)) },
                label = stringResource(TaskRes.string.field_task_name),
                singleLine = true,
            )

            // カテゴリ選択
            if (state.categories.isNotEmpty()) {
                Text(
                    text = "カテゴリ",
                    style = MaterialTheme.typography.labelLarge,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                    verticalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                ) {
                    // "なし" オプション
                    FilterChip(
                        selected = state.categoryId == null,
                        onClick = { viewModel.onIntent(TaskFormIntent.UpdateCategory(null)) },
                        label = { Text("なし") },
                        modifier = Modifier.bounceClick(),
                    )
                    // カテゴリチップ
                    state.categories.forEach { category ->
                        FilterChip(
                            selected = state.categoryId == category.id,
                            onClick = { viewModel.onIntent(TaskFormIntent.UpdateCategory(category.id)) },
                            label = { Text(category.name) },
                            modifier = Modifier.bounceClick(),
                        )
                    }
                }
            }

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
                    .clip(RoundedCornerShape(YatteSpacing.md))
                    .bounceClick(onTap = { showTimeSheet = true })
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(YatteSpacing.md),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = YatteSpacing.md)
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
                    horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                    verticalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
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

            // アラーム音選択
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            val soundPickerLauncher = rememberSoundPickerLauncher(
                onResult = { uri ->
                    if (uri != null) {
                        viewModel.onIntent(TaskFormIntent.UpdateSoundUri(uri))
                    }
                }
            )
            SoundPicker(
                currentSoundUri = state.soundUri,
                onSelectSound = { soundPickerLauncher.launch() },
                onClearSound = { viewModel.onIntent(TaskFormIntent.UpdateSoundUri(null)) },
                title = stringResource(TaskRes.string.label_notification_sound),
                selectedText = state.soundName ?: stringResource(TaskRes.string.sound_selected),
                defaultText = stringResource(TaskRes.string.sound_default),
                selectButtonText = stringResource(TaskRes.string.sound_select),
                clearContentDescription = stringResource(TaskRes.string.sound_clear),
            )

            Spacer(modifier = Modifier.height(YatteSpacing.xl))
        }
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
