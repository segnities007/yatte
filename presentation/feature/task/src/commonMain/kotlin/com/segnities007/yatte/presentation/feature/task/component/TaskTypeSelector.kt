package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.task_type_one_time
import yatte.presentation.feature.task.generated.resources.task_type_weekly
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

/**
 * タスクタイプ選択（単発/週次繰り返し）
 */
@Composable
fun TaskTypeSelector(
    selectedType: TaskType,
    onTypeSelected: (TaskType) -> Unit,
    modifier: Modifier = Modifier,
) {
    YatteSegmentedButtonRow(
        options = TaskType.entries.toList(),
        selectedIndex = TaskType.entries.indexOf(selectedType),
        onOptionSelected = { _, type -> onTypeSelected(type) },
        content = { type ->
            Text(
                if (type == TaskType.ONE_TIME) {
                    stringResource(TaskRes.string.task_type_one_time)
                } else {
                    stringResource(TaskRes.string.task_type_weekly)
                }
            )
        },
        modifier = modifier.fillMaxWidth(),
    )
}
