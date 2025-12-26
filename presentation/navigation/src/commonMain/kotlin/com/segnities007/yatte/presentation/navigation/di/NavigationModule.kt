package com.segnities007.yatte.presentation.navigation.di

import org.koin.dsl.module

/**
 * ナビゲーションモジュールのDI定義
 * JetBrains Navigation Compose を使用するため、NavHostController は Composable 内で作成する
 */
val navigationModule = module {
    // NavHostController は rememberNavController() で作成されるため、DIでは登録しない
    // 必要に応じてナビゲーション関連のユーティリティをここに追加可能
}
