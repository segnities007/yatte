package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.task.model.TaskType
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

/**
 * タスクタイプ選択（単発/週次繰り返し）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTypeSelector(
    selectedType: TaskType,
    onTypeSelected: (TaskType) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        TaskType.entries.forEachIndexed { index, type ->
            SegmentedButton(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
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
}
