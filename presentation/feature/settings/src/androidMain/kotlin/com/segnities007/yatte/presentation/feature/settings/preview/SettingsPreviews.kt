package com.segnities007.yatte.presentation.feature.settings.preview

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.component.card.YatteSectionCard
import com.segnities007.yatte.presentation.designsystem.component.list.YatteSwitchRow

@Preview
@Composable
private fun SettingsSectionWithSwitchPreview() {
    YatteTheme {
        YatteSectionCard(
            modifier = Modifier.padding(16.dp),
            icon = Icons.Default.Notifications,
            title = "Notifications",
        ) {
            YatteSwitchRow(
                title = "Enable Notifications",
                subtitle = "Receive alerts",
                checked = true,
                onCheckedChange = {}
            )
        }
    }
}
