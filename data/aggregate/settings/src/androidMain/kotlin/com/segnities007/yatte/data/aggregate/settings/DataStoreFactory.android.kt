package com.segnities007.yatte.data.aggregate.settings

import android.content.Context

private lateinit var appContext: Context

/**
 * Android用のContextを初期化
 */
fun initializeDataStore(context: Context) {
    appContext = context.applicationContext
}

actual fun getDataStorePath(): String {
    return appContext.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
}
