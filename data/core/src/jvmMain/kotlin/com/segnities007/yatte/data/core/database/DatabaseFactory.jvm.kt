package com.segnities007.yatte.data.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".yatte/${AppDatabase.DATABASE_NAME}")
    dbFile.parentFile?.mkdirs()
    return Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
}

