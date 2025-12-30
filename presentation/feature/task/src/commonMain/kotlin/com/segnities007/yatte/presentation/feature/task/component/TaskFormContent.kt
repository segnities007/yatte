package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.task.TaskFormIntent
import com.segnities007.yatte.presentation.feature.task.TaskFormState

@Composable
fun TaskFormContent(
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
