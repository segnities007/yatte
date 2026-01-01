package com.segnities007.yatte.presentation.designsystem.component.feedback

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape


/**
 * Yatte統一スナックバーホスト
 */
@Composable
fun YatteSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionColor: Color = MaterialTheme.colorScheme.inversePrimary,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        Snackbar(
            snackbarData = data,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            actionColor = actionColor,
            actionContentColor = actionContentColor,
            dismissActionContentColor = dismissActionContentColor,
        )
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun YatteSnackbarHostPreview() {
    MaterialTheme {
        YatteSnackbarHost(
            hostState = SnackbarHostState(),
        )
    }
}
