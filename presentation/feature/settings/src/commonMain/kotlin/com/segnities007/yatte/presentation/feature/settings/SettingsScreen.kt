package com.segnities007.yatte.presentation.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.nav_settings
import yatte.presentation.core.generated.resources.Res as CoreRes
import yatte.presentation.feature.settings.generated.resources.*
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    actions: SettingsActions,
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> actions.onBack()
                is SettingsEvent.ShowError -> onShowSnackbar(event.message)
                is SettingsEvent.ShowSaveSuccess -> onShowSnackbar(getString(SettingsRes.string.snackbar_settings_saved))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = { Text(stringResource(CoreRes.string.nav_settings)) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            // 通知設定
            Text(
                text = stringResource(SettingsRes.string.section_notifications),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.notification_sound_title)) },
                supportingContent = { Text(stringResource(SettingsRes.string.notification_sound_desc)) },
                trailingContent = {
                    Switch(
                        checked = state.settings.notificationSound,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationSound(it)) },
                    )
                },
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.notification_vibration_title)) },
                supportingContent = { Text(stringResource(SettingsRes.string.notification_vibration_desc)) },
                trailingContent = {
                    Switch(
                        checked = state.settings.notificationVibration,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationVibration(it)) },
                    )
                },
            )

            ListItem(
                headlineContent = {
                    Text(
                        stringResource(
                            SettingsRes.string.default_minutes_before,
                            state.settings.defaultMinutesBefore,
                        )
                    )
                },
                supportingContent = {
                    Slider(
                        value = state.settings.defaultMinutesBefore.toFloat(),
                        onValueChange = { viewModel.onIntent(SettingsIntent.UpdateMinutesBefore(it.toInt())) },
                        valueRange = 0f..60f,
                        steps = 11,
                    )
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // テーマ設定
            Text(
                text = stringResource(SettingsRes.string.section_appearance),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text(stringResource(SettingsRes.string.theme_title)) },
                supportingContent = {
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        ThemeMode.entries.forEachIndexed { index, mode ->
                            SegmentedButton(
                                selected = state.settings.themeMode == mode,
                                onClick = { viewModel.onIntent(SettingsIntent.UpdateThemeMode(mode)) },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = ThemeMode.entries.size,
                                ),
                            ) {
                                Text(mode.toDisplayString())
                            }
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun ThemeMode.toDisplayString(): String = when (this) {
    ThemeMode.LIGHT -> stringResource(SettingsRes.string.theme_light)
    ThemeMode.DARK -> stringResource(SettingsRes.string.theme_dark)
    ThemeMode.SYSTEM -> stringResource(SettingsRes.string.theme_system)
}
