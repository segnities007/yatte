package com.segnities007.yatte.presentation.navigation

/**
 * presentation:navigation モジュール
 *
 * JetBrains Navigation Compose を使用したナビゲーション実装
 *
 * 主なコンポーネント:
 * - Route.kt: 型安全なルート定義（kotlinx.serialization）
 * - AppNavHost: NavHost を使用した画面遷移管理
 *
 * 使用方法:
 * ```kotlin
 * val navController = rememberNavController()
 * AppNavHost(navController = navController)
 *
 * // ナビゲーション
 * navController.navigate(HomeRoute)
 * navController.navigate(EditTaskRoute(taskId = "123"))
 * navController.popBackStack()
 * ```
 */
