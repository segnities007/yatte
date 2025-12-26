package com.segnities007.yatte.presentation.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberFilePickerLauncher(
    type: FileType,
    onResult: (String?) -> Unit,
): FilePickerLauncher {
    return remember {
        object : FilePickerLauncher {
            override fun launch() {
                // TODO: Implement iOS FilePicker (e.g. using UIDocumentPickerViewController)
                println("FilePicker not implemented for iOS yet.")
            }
        }
    }
}
