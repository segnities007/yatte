package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

/**
 * 時間入力コンポーネント（時:分）
 */
@Composable
fun TimeInput(
    time: LocalTime,
    onTimeChange: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = time.hour.toString().padStart(2, '0'),
            onValueChange = { input ->
                val hour = input.filter { it.isDigit() }.take(2).toIntOrNull() ?: 0
                if (hour in 0..23) {
                    onTimeChange(LocalTime(hour, time.minute))
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
        OutlinedTextField(
            value = time.minute.toString().padStart(2, '0'),
            onValueChange = { input ->
                val minute = input.filter { it.isDigit() }.take(2).toIntOrNull() ?: 0
                if (minute in 0..59) {
                    onTimeChange(LocalTime(time.hour, minute))
                }
            },
            label = { Text(stringResource(TaskRes.string.label_minute)) },
            modifier = Modifier.weight(1f),
            singleLine = true,
        )
    }
}
