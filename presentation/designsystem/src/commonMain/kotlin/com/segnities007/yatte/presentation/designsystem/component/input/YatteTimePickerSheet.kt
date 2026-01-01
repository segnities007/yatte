package com.segnities007.yatte.presentation.designsystem.component.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import com.segnities007.yatte.presentation.designsystem.component.display.YatteIcon
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.MaterialTheme
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
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteModalBottomSheet
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.designsystem.theme.LocalYatteOnPrimaryBrushColor
import kotlinx.coroutines.launch

private enum class TimePickerMode {
    Dial, Input
}

/**
 * テーマ追従型タイムピッカーシート
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YatteTimePickerSheet(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var mode by remember { mutableStateOf(TimePickerMode.Dial) }
    
    val scope = rememberCoroutineScope()
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )
    
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
                        YatteIcon(
                            imageVector = if (item == TimePickerMode.Dial) Icons.Default.Schedule else Icons.Default.Keyboard,
                            contentDescription = item.name,
                            modifier = Modifier.padding(end = YatteSpacing.xs)
                        )
                        YatteText(text = if (item == TimePickerMode.Dial) "Clock" else "Input")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = YatteSpacing.lg)
            )

            var minHeight by remember { mutableStateOf(Dp.Unspecified) }
            val density = LocalDensity.current

            // Selection content color (Strict Visibility Rule)
            val selectionColor = MaterialTheme.colorScheme.primary
            val selectionContentColor = LocalYatteOnPrimaryBrushColor.current

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = minHeight)
            ) {
                // Apply theme-aware colors to TimePicker via MaterialTheme wrapper and TimePickerDefaults
                MaterialTheme(
                    colorScheme = MaterialTheme.colorScheme.copy(
                        primary = selectionColor,
                        onPrimary = selectionContentColor,
                    )
                ) {
                    if (mode == TimePickerMode.Dial) {
                        YatteTimePicker(
                            state = timePickerState,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                if (minHeight == Dp.Unspecified) {
                                    minHeight = with(density) { coordinates.size.height.toDp() }
                                }
                            },
                            colors = TimePickerDefaults.colors(
                                clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                                clockDialSelectedContentColor = selectionContentColor,
                                clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                                selectorColor = selectionColor,
                                containerColor = MaterialTheme.colorScheme.surface,
                                periodSelectorBorderColor = selectionColor,
                                periodSelectorSelectedContainerColor = selectionColor,
                                periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                periodSelectorSelectedContentColor = selectionContentColor,
                                periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                timeSelectorSelectedContainerColor = selectionColor,
                                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                timeSelectorSelectedContentColor = selectionContentColor,
                                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )
                    } else {
                        YatteTimeInput(
                            state = timePickerState,
                            colors = TimePickerDefaults.colors(
                                periodSelectorBorderColor = selectionColor,
                                periodSelectorSelectedContainerColor = selectionColor,
                                periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                periodSelectorSelectedContentColor = selectionContentColor,
                                periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                timeSelectorSelectedContainerColor = selectionColor,
                                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surface,
                                timeSelectorSelectedContentColor = selectionContentColor,
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
                            onConfirm(timePickerState.hour, timePickerState.minute)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                fillContainer = true
            )
            
            Spacer(modifier = Modifier.height(YatteSpacing.lg))
        }
    }
}
