package com.segnities007.yatte.presentation.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

/**
 * 設定画面から実行可能なナビゲーションアクションの定義。
 */
data class SettingsActions(
    val onBack: () -> Unit,
)

/**
 * 設定画面のナビゲーショングラフを構築するビルダー関数。
 */
fun NavGraphBuilder.settingsScreen(
    actions: SettingsActions,
    onShowSnackbar: (String) -> Unit,
) {
    composable<SettingsRoute> {
        SettingsScreen(
            actions = actions,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
