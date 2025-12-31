package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.segnities007.yatte.presentation.core.sound.rememberSoundPickerLauncher
import androidx.compose.ui.Modifier
import com.segnities007.yatte.domain.aggregate.settings.model.VibrationPattern
import com.segnities007.yatte.presentation.core.sound.SoundPickerLauncher
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSegmentedButtonRow
import com.segnities007.yatte.presentation.designsystem.component.input.YatteSwitchAccent
import com.segnities007.yatte.presentation.designsystem.component.layout.YatteSoundPicker
import com.segnities007.yatte.presentation.designsystem.component.list.YatteSliderRow
import com.segnities007.yatte.presentation.designsystem.component.list.YatteSwitchRow
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing
import com.segnities007.yatte.presentation.feature.settings.SettingsIntent
import com.segnities007.yatte.presentation.feature.settings.SettingsState
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.custom_sound_clear
import yatte.presentation.feature.settings.generated.resources.custom_sound_default
import yatte.presentation.feature.settings.generated.resources.custom_sound_select
import yatte.presentation.feature.settings.generated.resources.custom_sound_selected
import yatte.presentation.feature.settings.generated.resources.custom_sound_title
import yatte.presentation.feature.settings.generated.resources.default_minutes_before
import yatte.presentation.feature.settings.generated.resources.notification_sound_desc
import yatte.presentation.feature.settings.generated.resources.notification_sound_title
import yatte.presentation.feature.settings.generated.resources.notification_vibration_desc
import yatte.presentation.feature.settings.generated.resources.notification_vibration_title
import yatte.presentation.feature.settings.generated.resources.section_notifications
import yatte.presentation.feature.settings.generated.resources.snooze_duration_title
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_heartbeat
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_long
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_normal
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_short
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_sos
import yatte.presentation.feature.settings.generated.resources.vibration_pattern_title
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@Composable
fun SettingsNotificationSection(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    soundPickerLauncher: SoundPickerLauncher,
) {
    YatteSectionCard(
        icon = Icons.Default.Notifications,
        title = stringResource(SettingsRes.string.section_notifications),
    ) {
        YatteSwitchRow(
            title = stringResource(SettingsRes.string.notification_sound_title),
            subtitle = stringResource(SettingsRes.string.notification_sound_desc),
            checked = state.settings.notificationSound,
            onCheckedChange = { onIntent(SettingsIntent.ToggleNotificationSound(it)) },
            accent = YatteSwitchAccent.Sound,
        )

        YatteSwitchRow(
            title = stringResource(SettingsRes.string.notification_vibration_title),
            subtitle = stringResource(SettingsRes.string.notification_vibration_desc),
            checked = state.settings.notificationVibration,
            onCheckedChange = { onIntent(SettingsIntent.ToggleNotificationVibration(it)) },
            accent = YatteSwitchAccent.Vibration,
        )

        YatteSliderRow(
            title = stringResource(
                SettingsRes.string.default_minutes_before,
                state.settings.defaultMinutesBefore,
            ),
            value = state.settings.defaultMinutesBefore,
            valueRange = 1f..60f,
            steps = 60,
            onValueChange = { onIntent(SettingsIntent.UpdateMinutesBefore(it)) },
        )

        YatteSliderRow(
            title = stringResource(
                SettingsRes.string.snooze_duration_title,
                state.settings.snoozeDuration,
            ),
            value = state.settings.snoozeDuration,
            valueRange = 1f..60f,
            steps = 60,
            onValueChange = { onIntent(SettingsIntent.UpdateSnoozeDuration(it)) },
        )

        if (state.settings.notificationVibration) {
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            Text(
                text = stringResource(SettingsRes.string.vibration_pattern_title),
                style = MaterialTheme.typography.bodyMedium,
            )
            YatteSegmentedButtonRow(
                options = VibrationPattern.entries.toList(),
                selectedIndex = VibrationPattern.entries.indexOf(state.settings.vibrationPattern),
                onOptionSelected = { _, pattern -> onIntent(SettingsIntent.UpdateVibrationPattern(pattern)) },
                content = { Text(it.toUiLabel()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = YatteSpacing.xs),
            )
        }

        if (state.settings.notificationSound) {
            Spacer(modifier = Modifier.height(YatteSpacing.xs))
            YatteSoundPicker(
                currentSoundUri = state.settings.customSoundUri,
                onSelectSound = { soundPickerLauncher.launch() },
                onClearSound = { onIntent(SettingsIntent.UpdateCustomSoundUri(null)) },
                title = stringResource(SettingsRes.string.custom_sound_title),
                selectedText = stringResource(SettingsRes.string.custom_sound_selected),
                defaultText = stringResource(SettingsRes.string.custom_sound_default),
                selectButtonText = stringResource(SettingsRes.string.custom_sound_select),
                clearContentDescription = stringResource(SettingsRes.string.custom_sound_clear),
            )
        }
    }
}

@Composable
private fun VibrationPattern.toUiLabel(): String = when (this) {
    VibrationPattern.NORMAL -> stringResource(SettingsRes.string.vibration_pattern_normal)
    VibrationPattern.SHORT -> stringResource(SettingsRes.string.vibration_pattern_short)
    VibrationPattern.LONG -> stringResource(SettingsRes.string.vibration_pattern_long)
    VibrationPattern.SOS -> stringResource(SettingsRes.string.vibration_pattern_sos)
    VibrationPattern.HEARTBEAT -> stringResource(SettingsRes.string.vibration_pattern_heartbeat)
}

@Composable
@Preview
fun SettingsNotificationSectionPreview() {
    MaterialTheme {
        val soundPickerLauncher = rememberSoundPickerLauncher {}
        SettingsNotificationSection(
            state = SettingsState(),
            onIntent = {},
            soundPickerLauncher = soundPickerLauncher
        )
    }
}
