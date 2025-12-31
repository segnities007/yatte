package com.segnities007.yatte.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Yatteアプリの統一されたスペーシングトークン
 *
 * Usage:
 * ```kotlin
 * Modifier.padding(YatteSpacing.md)
 * Arrangement.spacedBy(YatteSpacing.sm)
 * ```
 */
@Immutable
data class YatteSpacingTokens(
    /** 4.dp - マイクロスペース（アイコンとテキスト間等） */
    val xxs: Dp = 4.dp,
    /** 8.dp - 小スペース */
    val xs: Dp = 8.dp,
    /** 12.dp - 中小スペース */
    val sm: Dp = 12.dp,
    /** 16.dp - 標準スペース（デフォルト） */
    val md: Dp = 16.dp,
    /** 24.dp - 大スペース */
    val lg: Dp = 24.dp,
    /** 32.dp - 特大スペース */
    val xl: Dp = 32.dp,
    /** 48.dp - セクション間 */
    val xxl: Dp = 48.dp,
    
    /** 1.dp - Divider thickness */
    val divider: Dp = 1.dp,
)

/**
 * デフォルトスペーシングインスタンス
 */
object YatteSpacing {
    val xxs: Dp = 4.dp
    val xs: Dp = 8.dp
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 24.dp
    val xl: Dp = 32.dp
    val xxl: Dp = 48.dp
    val divider: Dp = 1.dp
}

/**
 * CompositionLocal for spacing tokens
 */
val LocalYatteSpacing = staticCompositionLocalOf { YatteSpacingTokens() }

/**
 * 現在のスペーシングトークンを取得
 */
val spacing: YatteSpacingTokens
    @Composable
    get() = LocalYatteSpacing.current
