package com.segnities007.yatte.presentation.core.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf

/**
 * 現在のHeader設定を保持するCompositionLocal。
 * AppNavHostレベルで設定され、グローバルHeaderの表示内容を決定する。
 */
val LocalHeaderConfig = compositionLocalOf { HeaderConfig() }

/**
 * Header設定を更新するための関数を提供するCompositionLocal。
 * 各画面はこれを使ってHeader内容を設定する。
 */
val LocalSetHeaderConfig = compositionLocalOf<(HeaderConfig) -> Unit> { {} }

/**
 * 現在の画面のHeader設定を更新するユーティリティComposable。
 * LaunchedEffectの代わりに副作用なく呼び出すことができる。
 */
@Composable
fun SetHeaderConfig(config: HeaderConfig) {
    val setConfig = LocalSetHeaderConfig.current
    setConfig(config)
}
