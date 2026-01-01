package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.segnities007.yatte.presentation.designsystem.theme.YatteTheme
import com.segnities007.yatte.presentation.designsystem.component.display.YatteText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButton
import com.segnities007.yatte.presentation.designsystem.component.button.YatteButtonStyle
import com.segnities007.yatte.presentation.designsystem.component.feedback.YatteDialog

@Composable
fun LicenseDialog(
    library: Library,
    onDismiss: () -> Unit,
) {
    YatteDialog(
        title = library.name,
        onDismiss = onDismiss,
        confirmButton = {
            YatteButton(
                onClick = onDismiss,
                text = "Close",
                style = YatteButtonStyle.Secondary,
            )
        },
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            YatteText(
                text = "Version: ${library.artifactVersion ?: "Unknown"}",
                style = YatteTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (library.licenses.isNotEmpty()) {
                library.licenses.forEach { license ->
                    YatteText(
                        text = license.name,
                        style = YatteTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    license.url?.let { url ->
                         YatteText(
                            text = url,
                            style = YatteTheme.typography.bodySmall,
                            color = YatteTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            } else {
                YatteText("No license information available.")
            }
        }
    }
}
