package com.segnities007.yatte.presentation.feature.category

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CategoryRoute

fun NavGraphBuilder.categoryScreen(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    composable<CategoryRoute> {
        CategoryScreen(
            onBack = onBack,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

fun NavController.navigateToCategory() {
    navigate(CategoryRoute)
}
