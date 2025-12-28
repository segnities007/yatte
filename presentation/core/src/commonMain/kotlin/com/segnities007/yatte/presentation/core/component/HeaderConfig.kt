package com.segnities007.yatte.presentation.core.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * グローバルHeaderの内容を定義するimmutableなデータクラス。
 * 各画面からHeaderの表示内容を設定するために使用する。
 *
 * @param title ヘッダーのタイトル部分（中央配置）
 * @param navigationIcon 左側のナビゲーションアイコン（戻るボタンなど）
 * @param actions 右側のアクションボタン群
 */
@Immutable
data class HeaderConfig(
    val title: @Composable () -> Unit = {},
    val navigationIcon: @Composable (() -> Unit)? = null,
    val actions: @Composable RowScope.() -> Unit = {},
)
