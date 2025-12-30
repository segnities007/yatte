package com.segnities007.yatte.presentation.designsystem.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segnities007.yatte.presentation.designsystem.component.navigation.YatteFloatingHeader
import com.segnities007.yatte.presentation.designsystem.component.navigation.YatteFloatingHeaderDefaults

/**
 * スクロール連動対応の共通画面コンテナ。
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
 */
object YatteScrollVisibilityController {
    fun createNestedScrollConnection(
        onVisibilityChange: (Boolean) -> Unit,
        scrollThreshold: Float = 5f,
    ): NestedScrollConnection {
        return object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
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

@Preview(showBackground = true)
@Composable
private fun YatteScaffoldPreview() {
    YatteScaffold(
        isNavigationVisible = true,
        contentPadding = PaddingValues(0.dp),
        header = { isVisible ->
            YatteFloatingHeader(
                title = { Text("Preview Header") },
                isVisible = isVisible
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Content Area with padding: $padding")
        }
    }
}
