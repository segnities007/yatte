package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteModalBottomSheet
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.component.input.YatteTimeInput
import com.segnities007.yatte.presentation.designsystem.component.input.YatteTimePicker
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

private enum class TimePickerMode {
    Dial, Input
}

private val SelectorYellow = Color(0xFFFBC02D)
private val SelectorContent = Color.Black

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
    
    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    YatteModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = YatteSpacing.md),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Switcher
            YatteSegmentedButtonRow(
                options = TimePickerMode.entries.toList(),
                selectedIndex = TimePickerMode.entries.indexOf(mode),
                onOptionSelected = { _, item -> mode = item },
                content = { item ->
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
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = YatteSpacing.lg)
            )

            // Picker - dynamic height based on content
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
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
                        YatteTimePicker(
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
                        YatteTimeInput(
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
                text = "OK",
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onConfirm(LocalTime(timePickerState.hour, timePickerState.minute))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Navigation bar padding + extra space
            Spacer(modifier = Modifier.height(navigationBarPadding + YatteSpacing.lg))
        }
    }
}


