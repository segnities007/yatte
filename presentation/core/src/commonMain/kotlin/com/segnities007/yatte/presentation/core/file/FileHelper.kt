package com.segnities007.yatte.presentation.core.file

import androidx.compose.runtime.Composable

interface FileHelper {
    /**
     * ファイルを保存する（エクスポート）
     * @param fileName デフォルトファイル名
     * @param content 保存するテキスト内容
     */
    fun saveFile(fileName: String, content: String, onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {})

    /**
     * ファイルを読み込む（インポート）
     * @param onLoaded 読み込み成功時のコールバック（内容のテキスト）
     */
    fun loadFile(onLoaded: (String) -> Unit, onError: (Throwable) -> Unit = {})
}

@Composable
expect fun rememberFileHelper(): FileHelper
