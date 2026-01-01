package com.segnities007.yatte.presentation.feature.settings.component

import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.core.generated.resources.nav_settings
import yatte.presentation.core.generated.resources.Res as CoreRes

@Composable
fun SettingsHeader() {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val settingsTitle = stringResource(CoreRes.string.nav_settings)
    
    val headerConfig = remember(settingsTitle) {
        HeaderConfig(title = { YatteText(settingsTitle) })
    }
    
    SideEffect {
        setHeaderConfig(headerConfig)
    }
}
