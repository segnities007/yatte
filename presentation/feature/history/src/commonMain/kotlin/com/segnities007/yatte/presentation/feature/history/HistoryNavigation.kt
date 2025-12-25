package com.segnities007.yatte.presentation.feature.history

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HistoryRoute

/**
 * 履歴画面から実行可能なナビゲーションアクションの定義。
 */
data class HistoryActions(
    val onBack: () -> Unit,
)

/**
 * 履歴画面のナビゲーショングラフを構築するビルダー関数。
 */
fun NavGraphBuilder.historyScreen(
    actions: HistoryActions,
    onShowSnackbar: (String) -> Unit,
) {
    composable<HistoryRoute> {
        HistoryScreen(
            actions = actions,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
