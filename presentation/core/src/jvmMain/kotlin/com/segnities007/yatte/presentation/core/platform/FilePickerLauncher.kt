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
                // TODO: Implement Desktop FilePicker (e.g. using JFileChooser or AWT FileDialog)
                println("FilePicker not implemented for Desktop yet.")
            }
        }
    }
}
