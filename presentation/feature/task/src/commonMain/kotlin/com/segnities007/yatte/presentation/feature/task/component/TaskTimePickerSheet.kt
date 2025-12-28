package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.presentation.designsystem.animation.bounceClick
import com.segnities007.yatte.presentation.designsystem.component.YatteButton
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.datetime.LocalTime

private enum class TimePickerMode {
    Dial, Input
}

private val SelectorYellow = Color(0xFFFBC02D)
private val SelectorContent = Color.Black
private val ClockDialBg = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePickerSheet(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mode by remember { mutableStateOf(TimePickerMode.Dial) }
    
    val scope = rememberCoroutineScope()
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = YatteSpacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Switcher
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = YatteSpacing.lg)
            ) {
                TimePickerMode.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = mode == item,
                        onClick = { mode = item },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = TimePickerMode.entries.size),
                        modifier = Modifier.bounceClick()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (item == TimePickerMode.Dial) Icons.Default.Schedule else Icons.Default.Keyboard,
                                contentDescription = item.name,
                                modifier = Modifier.padding(end = YatteSpacing.xs)
                            )
                            Text(text = if (item == TimePickerMode.Dial) "Clock" else "Input")
                        }
                    }
                }
            }

            // Picker
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
            ) {
                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(
                        primary = SelectorYellow,
                        onPrimary = SelectorContent,
                        tertiary = SelectorYellow,
                        onTertiary = SelectorContent,
                    )
                ) {
                    if (mode == TimePickerMode.Dial) {
                        TimePicker(
                            state = timePickerState,
                            colors = TimePickerDefaults.colors(
                                clockDialColor = MaterialTheme.colorScheme.surface,
                                clockDialSelectedContentColor = SelectorContent,
                                clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectorColor = SelectorYellow,
                                containerColor = MaterialTheme.colorScheme.surface,
                                periodSelectorBorderColor = SelectorYellow,
                                periodSelectorSelectedContainerColor = SelectorYellow,
                                periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                periodSelectorSelectedContentColor = SelectorContent,
                                periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                timeSelectorSelectedContainerColor = SelectorYellow,
                                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                timeSelectorSelectedContentColor = SelectorContent,
                                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )
                    } else {
                        TimeInput(
                            state = timePickerState,
                            colors = TimePickerDefaults.colors(
                                periodSelectorBorderColor = SelectorYellow,
                                periodSelectorSelectedContainerColor = SelectorYellow,
                                periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                periodSelectorSelectedContentColor = SelectorContent,
                                periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                timeSelectorSelectedContainerColor = SelectorYellow,
                                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                timeSelectorSelectedContentColor = SelectorContent,
                                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(YatteSpacing.lg))

            // Confirm Button
            YatteButton(
                text = "完了",
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onConfirm(LocalTime(timePickerState.hour, timePickerState.minute))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(YatteSpacing.xxl))
        }
    }
}

