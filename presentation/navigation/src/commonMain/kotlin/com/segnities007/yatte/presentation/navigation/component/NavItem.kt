package com.segnities007.yatte.presentation.navigation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import yatte.presentation.core.generated.resources.Res
import yatte.presentation.core.generated.resources.nav_history
import yatte.presentation.core.generated.resources.nav_home
import yatte.presentation.core.generated.resources.nav_manage
import yatte.presentation.core.generated.resources.nav_settings

/**
 * ナビゲーション項目
 */
enum class NavItem(
    val icon: ImageVector,
    val labelRes: StringResource,
) {
    HOME(Icons.Default.Home, Res.string.nav_home),
    MANAGE(Icons.AutoMirrored.Filled.List, Res.string.nav_manage),
    HISTORY(Icons.Default.History, Res.string.nav_history),
    SETTINGS(Icons.Default.Settings, Res.string.nav_settings),
}
