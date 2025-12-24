package com.segnites007.yatte

/**
 * ナビゲーションルート定義
 */
sealed class Route(
    val route: String,
) {
    data object Home : Route("home")

    data object AddTask : Route("task/add")

    data class EditTask(
        val taskId: String,
    ) : Route("task/edit/{taskId}") {
        companion object {
            const val ROUTE_PATTERN = "task/edit/{taskId}"

            fun createRoute(taskId: String) = "task/edit/$taskId"
        }
    }

    data object History : Route("history")

    data object Settings : Route("settings")
}
