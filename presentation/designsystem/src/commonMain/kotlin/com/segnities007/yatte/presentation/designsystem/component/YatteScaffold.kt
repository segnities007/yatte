package com.segnities007.yatte.presentation.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.dp

/**
 * スクロール連動対応の共通画面コンテナ。
 *
 * このScaffoldを使用することで、コンテンツのスクロールに応じて
 * ヘッダーとナビゲーションバーの表示/非表示を自動制御できる。
 *
 * @param isNavigationVisible 外部から渡される表示状態（AppNavHostから）
 * @param header ヘッダーコンテンツ（FloatingHeaderBar等）。`isVisible`パラメータが渡される。
 * @param contentPadding コンテンツ用の基本パディング（AppNavHostから）
 * @param modifier Modifier
 * @param content メインコンテンツ。`contentPadding`パラメータが渡される。
 */
@Composable
fun YatteScaffold(
    isNavigationVisible: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    header: @Composable BoxScope.(isVisible: Boolean) -> Unit = {},
    content: @Composable BoxScope.(contentPadding: PaddingValues) -> Unit,
) {
    // ヘッダー高さを計算（statusBar + ヘッダー + マージン）
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val headerHeight = YatteFloatingHeaderDefaults.ContainerHeight +
            YatteFloatingHeaderDefaults.TopMargin +
            YatteFloatingHeaderDefaults.BottomSpacing
    
    // システムナビゲーションバーの高さを取得 + デフォルト余白
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val defaultBottomPadding = 8.dp

    val listContentPadding = PaddingValues(
        top = statusBarHeight + headerHeight,
        bottom = contentPadding.calculateBottomPadding() + navigationBarHeight + defaultBottomPadding,
        start = 16.dp,
        end = 16.dp,
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // コンテンツ（背面）
        content(listContentPadding)

        // ヘッダー（前面）
        header(isNavigationVisible)
    }
}

/**
 * スクロール状態を管理するためのユーティリティオブジェクト。
 * AppNavHostで使用されるNestedScrollConnectionのロジックを提供。
 */
object YatteScrollVisibilityController {
    /**
     * スクロール方向に基づいてUI要素の表示/非表示を制御するNestedScrollConnectionを作成。
     *
     * @param onVisibilityChange 表示状態が変化した際に呼ばれるコールバック
     * @param scrollThreshold スクロール検知の閾値（デフォルト: 5f）
     */
    fun createNestedScrollConnection(
        onVisibilityChange: (Boolean) -> Unit,
        scrollThreshold: Float = 5f,
    ): NestedScrollConnection {
        return object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 下スクロール（available.y < 0）で非表示、上スクロール（available.y > 0）で表示
                if (available.y < -scrollThreshold) {
                    onVisibilityChange(false)
                } else if (available.y > scrollThreshold) {
                    onVisibilityChange(true)
                }
                return Offset.Zero
            }
        }
    }
}
