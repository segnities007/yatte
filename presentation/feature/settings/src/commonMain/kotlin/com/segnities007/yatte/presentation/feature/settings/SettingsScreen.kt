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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.domain.aggregate.settings.model.ThemeMode
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onShowSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> onNavigateBack()
                is SettingsEvent.ShowError -> onShowSnackbar(event.message)
                is SettingsEvent.ShowSaveSuccess -> onShowSnackbar("設定を保存しました")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(SettingsIntent.NavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
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
                text = "通知",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text("通知音") },
                supportingContent = { Text("タスク通知時に音を鳴らす") },
                trailingContent = {
                    Switch(
                        checked = state.settings.notificationSound,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationSound(it)) },
                    )
                },
            )

            ListItem(
                headlineContent = { Text("バイブレーション") },
                supportingContent = { Text("タスク通知時に振動する") },
                trailingContent = {
                    Switch(
                        checked = state.settings.notificationVibration,
                        onCheckedChange = { viewModel.onIntent(SettingsIntent.ToggleNotificationVibration(it)) },
                    )
                },
            )

            ListItem(
                headlineContent = { Text("デフォルト通知時間: ${state.settings.defaultMinutesBefore}分前") },
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
                text = "外観",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )

            ListItem(
                headlineContent = { Text("テーマ") },
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
                                Text(mode.toJapanese())
                            }
                        }
                    }
                },
            )
        }
    }
}

private fun ThemeMode.toJapanese(): String = when (this) {
    ThemeMode.LIGHT -> "ライト"
    ThemeMode.DARK -> "ダーク"
    ThemeMode.SYSTEM -> "システム"
}
