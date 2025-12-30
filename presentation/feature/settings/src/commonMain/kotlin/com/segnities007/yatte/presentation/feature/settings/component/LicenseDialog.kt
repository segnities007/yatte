package com.segnities007.yatte.presentation.feature.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.segnities007.yatte.presentation.designsystem.component.button.YatteTextButton
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
            YatteTextButton(
                onClick = onDismiss,
                text = "Close",
            )
        },
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Version: ${library.artifactVersion ?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (library.licenses.isNotEmpty()) {
                library.licenses.forEach { license ->
                    Text(
                        text = license.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    license.url?.let { url ->
                         Text(
                            text = url,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            } else {
                Text("No license information available.")
            }
        }
    }
}
