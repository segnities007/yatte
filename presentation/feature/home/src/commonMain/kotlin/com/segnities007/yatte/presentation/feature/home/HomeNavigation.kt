package com.segnities007.yatte.presentation.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

/**
 * Home画面から実行可能なナビゲーションアクションの定義。
 *
 * 仕様:
 * - 遷移先の知識を持たず、純粋なコールバックとして定義する（Actions Classパターン）
 */
data class HomeActions(
    val onAddTask: () -> Unit,
    val onHistory: () -> Unit,
    val onSettings: () -> Unit,
    val onEditTask: (String) -> Unit,
)

/**
 * Home画面のナビゲーショングラフを構築するビルダー関数。
 *
 * 仕様:
 * - `HomeRoute` をコンポーズする
 * - [actions] を通じてナビゲーションイベントを処理する
 */
fun NavGraphBuilder.homeScreen(
    actions: HomeActions,
    contentPadding: PaddingValues,
    isNavigationVisible: Boolean,
    onShowSnackbar: (String) -> Unit,
) {
    composable<HomeRoute> {
        HomeScreen(
            actions = actions,
            contentPadding = contentPadding,
            isNavigationVisible = isNavigationVisible,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
