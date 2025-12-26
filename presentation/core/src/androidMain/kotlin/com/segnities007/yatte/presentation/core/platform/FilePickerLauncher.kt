package com.segnities007.yatte.presentation.core.platform

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFilePickerLauncher(
    type: FileType,
    onResult: (String?) -> Unit,
): FilePickerLauncher {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                // 永続的なアクセス権限を取得（再起動後もアクセスできるように）
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, takeFlags)
                onResult(uri.toString())
            } catch (e: Exception) {
                // 権限取得失敗時などはURIだけ返す（一時的なアクセスは可能な場合がある）
                e.printStackTrace()
                onResult(uri.toString())
            }
        } else {
            // キャンセル時は何もしないかnullを返す
            // onResult(null) // キャンセルを通知したい場合は呼ぶ
        }
    }

    return remember {
        object : FilePickerLauncher {
            override fun launch() {
                val mimeType = when (type) {
                    FileType.Audio -> "audio/*"
                    FileType.Image -> "image/*"
                }
                launcher.launch(arrayOf(mimeType))
            }
        }
    }
}
