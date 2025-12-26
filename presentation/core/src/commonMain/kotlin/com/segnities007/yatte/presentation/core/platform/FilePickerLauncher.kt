package com.segnities007.yatte.presentation.core.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberFilePickerLauncher(
    type: FileType = FileType.Audio,
    onResult: (String?) -> Unit,
): FilePickerLauncher

interface FilePickerLauncher {
    fun launch()
}

enum class FileType {
    Audio,
    Image,
}
