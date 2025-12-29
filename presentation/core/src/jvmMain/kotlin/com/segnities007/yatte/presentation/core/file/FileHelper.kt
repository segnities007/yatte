package com.segnities007.yatte.presentation.core.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.SwingUtilities

class JvmFileHelper : FileHelper {
    override fun saveFile(
        fileName: String,
        content: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        SwingUtilities.invokeLater {
            val fileDialog = FileDialog(null as Frame?, "Save", FileDialog.SAVE)
            fileDialog.file = fileName
            fileDialog.isVisible = true

            if (fileDialog.directory != null && fileDialog.file != null) {
                try {
                    val file = File(fileDialog.directory, fileDialog.file)
                    file.writeText(content)
                    onSuccess()
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }

    override fun loadFile(
        onLoaded: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        SwingUtilities.invokeLater {
            val fileDialog = FileDialog(null as Frame?, "Load", FileDialog.LOAD)
            fileDialog.isVisible = true

            if (fileDialog.directory != null && fileDialog.file != null) {
                try {
                    val file = File(fileDialog.directory, fileDialog.file)
                    val content = file.readText()
                    onLoaded(content)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }
}

@Composable
actual fun rememberFileHelper(): FileHelper {
    return remember { JvmFileHelper() }
}
