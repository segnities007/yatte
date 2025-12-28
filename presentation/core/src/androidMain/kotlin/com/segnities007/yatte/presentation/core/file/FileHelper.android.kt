package com.segnities007.yatte.presentation.core.file

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
actual fun rememberFileHelper(): FileHelper {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    return rememberAndroidFileHelper(context, scope)
}

@Composable
private fun rememberAndroidFileHelper(
    context: Context,
    scope: CoroutineScope
): FileHelper {
    // 状態保持用のクラス（Composableの再構築に耐えるようにrememberするが、内部状態はCallbackで参照される）
    val helper = remember { AndroidFileHelperImpl(context, scope) }

    // Launcher定義 (Composition内で定義必須)
    
    // エクスポート用 (CreateDocument)
    val createLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        helper.onFileCreated(uri)
    }

    // インポート用 (OpenDocument)
    val openLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        helper.onFilePicked(uri)
    }

    // HelperにLauncherをセット（循環参照っぽいが、HelperはLauncherを呼び出すだけ）
    helper.setLaunchers(
        launchCreate = { name -> createLauncher.launch(name) },
        launchOpen = { openLauncher.launch(arrayOf("application/json")) }
    )
    
    return helper
}

private class AndroidFileHelperImpl(
    private val context: Context,
    private val scope: CoroutineScope
) : FileHelper {

    private var launchCreate: ((String) -> Unit)? = null
    private var launchOpen: (() -> Unit)? = null

    // 処理待ちのデータ
    private var pendingSaveContent: String? = null
    private var onSaveSuccess: (() -> Unit)? = null
    private var onSaveError: ((Throwable) -> Unit)? = null

    private var onLoadSuccess: ((String) -> Unit)? = null
    private var onLoadError: ((Throwable) -> Unit)? = null

    fun setLaunchers(
        launchCreate: (String) -> Unit,
        launchOpen: () -> Unit
    ) {
        this.launchCreate = launchCreate
        this.launchOpen = launchOpen
    }

    override fun saveFile(
        fileName: String,
        content: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        pendingSaveContent = content
        onSaveSuccess = onSuccess
        onSaveError = onError
        launchCreate?.invoke(fileName)
    }

    override fun loadFile(
        onLoaded: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        onLoadSuccess = onLoaded
        onLoadError = onError
        launchOpen?.invoke()
    }

    // Callback from Launcher
    fun onFileCreated(uri: Uri?) {
        if (uri == null) {
            // Cancelled
            return
        }
        val content = pendingSaveContent ?: return

        scope.launch(Dispatchers.IO) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    output.write(content.toByteArray())
                }
            }.onSuccess {
                scope.launch(Dispatchers.Main) { onSaveSuccess?.invoke() }
            }.onFailure { e ->
                scope.launch(Dispatchers.Main) { onSaveError?.invoke(e) }
            }
            // Cleanup
            pendingSaveContent = null
        }
    }

    fun onFilePicked(uri: Uri?) {
        if (uri == null) {
            // Cancelled
            return
        }
        
        scope.launch(Dispatchers.IO) {
            runCatching {
                val stringBuilder = StringBuilder()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line = reader.readLine()
                        while (line != null) {
                            stringBuilder.append(line).append("\n")
                            line = reader.readLine()
                        }
                    }
                }
                stringBuilder.toString()
            }.onSuccess { content ->
                scope.launch(Dispatchers.Main) { onLoadSuccess?.invoke(content) }
            }.onFailure { e ->
                scope.launch(Dispatchers.Main) { onLoadError?.invoke(e) }
            }
        }
    }
}
