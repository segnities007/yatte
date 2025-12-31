package com.segnities007.yatte.presentation.feature.task.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteSoundPicker
import com.segnities007.yatte.presentation.designsystem.component.list.YatteSliderRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import androidx.compose.material3.MaterialTheme
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import com.segnities007.yatte.presentation.feature.task.TaskFormIntent
import com.segnities007.yatte.presentation.feature.task.TaskFormState
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.task.generated.resources.label_notification_sound
import yatte.presentation.feature.task.generated.resources.notification_minutes_before
import yatte.presentation.feature.task.generated.resources.section_notification
import yatte.presentation.feature.task.generated.resources.sound_clear
import yatte.presentation.feature.task.generated.resources.sound_default
import yatte.presentation.feature.task.generated.resources.sound_select
import yatte.presentation.feature.task.generated.resources.sound_selected
import yatte.presentation.feature.task.generated.resources.Res as TaskRes

@Composable
fun TaskFormNotificationSection(
    state: TaskFormState,
    onIntent: (TaskFormIntent) -> Unit,
    soundPickerLauncher: SoundPickerLauncher,
) {
    YatteSectionCard(
        title = stringResource(TaskRes.string.section_notification),
        icon = Icons.Default.Notifications
    ) {
        YatteSliderRow(
            title = stringResource(TaskRes.string.notification_minutes_before, state.minutesBefore),
            value = state.minutesBefore,
            onValueChange = { onIntent(TaskFormIntent.UpdateMinutesBefore(it)) },
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
@Preview
fun TaskFormNotificationSectionPreview() {
    MaterialTheme {
        val soundPickerLauncher = rememberSoundPickerLauncher {}
        TaskFormNotificationSection(
            state = TaskFormState(),
            onIntent = {},
            soundPickerLauncher = soundPickerLauncher,
        )
    }
}
