package com.segnities007.yatte.presentation.core.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosFileHelper : FileHelper {
    override fun saveFile(
        fileName: String,
        content: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // TODO: Implement iOS file saving logic (e.g. using UIDocumentPickerViewController)
        // For now, we can just print to console or do nothing to pass compilation
        println("File saving not implemented for iOS yet. Content length: ${content.length}")
        onError(NotImplementedError("iOS file saving not implemented"))
    }

    override fun loadFile(
        onLoaded: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        // TODO: Implement iOS file loading logic
        println("File loading not implemented for iOS yet.")
        onError(NotImplementedError("iOS file loading not implemented"))
    }
}

@Composable
actual fun rememberFileHelper(): FileHelper {
    return remember { IosFileHelper() }
}
