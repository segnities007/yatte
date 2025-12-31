package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.segnities007.yatte.presentation.core.component.HeaderConfig
import com.segnities007.yatte.presentation.core.component.LocalSetHeaderConfig
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton
import org.jetbrains.compose.resources.stringResource
import yatte.presentation.feature.settings.generated.resources.license_screen_title
import yatte.presentation.feature.settings.generated.resources.Res as SettingsRes

@Composable
fun LicenseHeader(
    onBackClick: () -> Unit,
) {
    val setHeaderConfig = LocalSetHeaderConfig.current
    val title = stringResource(SettingsRes.string.license_screen_title)

    val headerConfig = remember(title, onBackClick) {
        HeaderConfig(
            title = { YatteText(title) },
            navigationIcon = {
                YatteIconButton(
                    onClick = onBackClick,
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            },
        )
    }

    SideEffect {
        setHeaderConfig(headerConfig)
    }
}

