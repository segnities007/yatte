package com.segnities007.yatte.data.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/${AppDatabase.DATABASE_NAME}"
    return Room.databaseBuilder<AppDatabase>(dbFilePath)
}
