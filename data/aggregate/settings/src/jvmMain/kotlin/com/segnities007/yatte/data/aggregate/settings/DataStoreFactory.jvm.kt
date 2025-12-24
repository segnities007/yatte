package com.segnities007.yatte.data.aggregate.settings

import java.io.File

actual fun getDataStorePath(): String {
    val userHome = System.getProperty("user.home")
    val dataDir = File(userHome, ".yatte")
    if (!dataDir.exists()) {
        dataDir.mkdirs()
    }
    return File(dataDir, DATA_STORE_FILE_NAME).absolutePath
}
