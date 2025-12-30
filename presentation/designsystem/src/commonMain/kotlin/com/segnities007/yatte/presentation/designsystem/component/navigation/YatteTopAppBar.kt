package com.segnities007.yatte.presentation.designsystem.component.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.segnities007.yatte.presentation.designsystem.component.button.YatteIconButton

/**
 * Yatte統一トップアプリバー
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YatteTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    navigationContentDescription: String? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                YatteIconButton(
                    icon = navigationIcon,
                    onClick = onNavigationClick,
                    contentDescription = navigationContentDescription ?: "Back",
                )
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun YatteTopAppBarPreview() {
    YatteTopAppBar(
        title = "Page Title",
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationClick = {},
        actions = {
            IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, contentDescription = null) }
        }
    )
}
