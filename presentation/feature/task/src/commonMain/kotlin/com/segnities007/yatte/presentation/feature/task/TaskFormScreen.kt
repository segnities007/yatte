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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
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
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.YatteSoundPicker
import com.segnities007.yatte.presentation.designsystem.component.YatteScaffold
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.YatteIconButton
import com.segnities007.yatte.presentation.designsystem.component.YatteTextField
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.component.YatteSectionCard
import com.segnities007.yatte.presentation.feature.task.component.TaskTimePickerSheet
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yatte.presentation.core.generated.resources.common_back
import yatte.presentation.core.generated.resources.common_delete
import yatte.presentation.core.generated.resources.common_save
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.task.generated.resources.*
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

    TaskFormSetupHeader(state = state, onIntent = viewModel::onIntent)
    TaskFormSetupSideEffects(viewModel = viewModel, actions = actions, onShowSnackbar = onShowSnackbar)

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

@Composable
private fun TaskFormSetupHeader(
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

@Composable
private fun TaskFormSetupSideEffects(
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TaskFormContent(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
    soundPickerLauncher: SoundPickerLauncher,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
                start = YatteSpacing.md,
                end = YatteSpacing.md,
            ),
        verticalArrangement = Arrangement.spacedBy(YatteSpacing.md),
    ) {
        Spacer(modifier = Modifier.height(YatteSpacing.xs))
        TaskFormBasicInfoSection(state = state, onIntent = onIntent)
        TaskFormScheduleSection(state = state, onIntent = onIntent)
        TaskFormNotificationSection(state = state, onIntent = onIntent, soundPickerLauncher = soundPickerLauncher)
        Spacer(modifier = Modifier.height(YatteSpacing.xl))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TaskFormBasicInfoSection(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
) {
    YatteSectionCard(
        title = stringResource(TaskRes.string.section_basic_info),
        icon = Icons.Default.Edit
    ) {
        YatteTextField(
            value = state.title,
            onValueChange = { onIntent(TaskFormIntent.UpdateTitle(it)) },
            label = stringResource(TaskRes.string.field_task_name),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        if (state.categories.isNotEmpty()) {
            Text(
                text = stringResource(TaskRes.string.section_category),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
            ) {
                FilterChip(
                    selected = state.categoryId == null,
                    onClick = { onIntent(TaskFormIntent.UpdateCategory(null)) },
                    label = { Text(stringResource(TaskRes.string.category_none)) },
                    modifier = Modifier.bounceClick(),
                )
                state.categories.forEach { category ->
                    FilterChip(
                        selected = state.categoryId == category.id,
                        onClick = { onIntent(TaskFormIntent.UpdateCategory(category.id)) },
                        label = { Text(category.name) },
                        modifier = Modifier.bounceClick(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TaskFormScheduleSection(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
) {
    YatteSectionCard(
        title = stringResource(TaskRes.string.section_schedule),
        icon = Icons.Default.Schedule,
    ) {
        Text(
             text = stringResource(TaskRes.string.label_execute_time),
             style = MaterialTheme.typography.labelMedium,
             color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(YatteSpacing.xs))

        var showTimeSheet by remember { mutableStateOf(false) }

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
                        text = stringResource(TaskRes.string.tap_to_change_time),
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
                    onIntent(TaskFormIntent.UpdateTime(newTime))
                    showTimeSheet = false
                }
            )
        }

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        Text(
            text = stringResource(TaskRes.string.label_task_type),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(YatteSpacing.xs))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            TaskType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = state.taskType == type,
                    onClick = { onIntent(TaskFormIntent.UpdateTaskType(type)) },
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

        if (state.taskType == TaskType.WEEKLY_LOOP) {
            Spacer(modifier = Modifier.height(YatteSpacing.md))
            Text(
                text = stringResource(TaskRes.string.label_repeat_weekdays),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
                verticalArrangement = Arrangement.spacedBy(YatteSpacing.xs),
            ) {
                DayOfWeek.entries.forEach { day ->
                    FilterChip(
                        selected = day in state.selectedWeekDays,
                        onClick = { onIntent(TaskFormIntent.ToggleWeekDay(day)) },
                        label = { Text(day.toDisplayString()) },
                        modifier = Modifier.bounceClick()
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskFormNotificationSection(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
    soundPickerLauncher: SoundPickerLauncher,
) {
    YatteSectionCard(
        title = stringResource(TaskRes.string.section_notification),
        icon = Icons.Default.Notifications
    ) {
        Text(
            text = stringResource(TaskRes.string.notification_minutes_before, state.minutesBefore),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Slider(
            value = state.minutesBefore.toFloat(),
            onValueChange = { onIntent(TaskFormIntent.UpdateMinutesBefore(it.toInt())) },
            valueRange = 0f..60f,
            steps = 11,
        )

        Spacer(modifier = Modifier.height(YatteSpacing.md))

        YatteSoundPicker(
            currentSoundUri = state.soundUri,
            onSelectSound = { soundPickerLauncher.launch() },
            onClearSound = { onIntent(TaskFormIntent.UpdateSoundUri(null)) },
            title = stringResource(TaskRes.string.label_notification_sound),
            selectedText = state.soundName ?: stringResource(TaskRes.string.sound_selected),
            defaultText = stringResource(TaskRes.string.sound_default),
            selectButtonText = stringResource(TaskRes.string.sound_select),
            clearContentDescription = stringResource(TaskRes.string.sound_clear),
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
