package com.segnities007.yatte.data.aggregate.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * DataStoreのファイルパスを取得する（プラットフォーム固有）
 */
expect fun getDataStorePath(): String

/**
 * DataStoreインスタンスを作成（共通）
 */
fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { getDataStorePath().toPath() },
    )
}

internal const val DATA_STORE_FILE_NAME = "yatte_settings.preferences_pb"
