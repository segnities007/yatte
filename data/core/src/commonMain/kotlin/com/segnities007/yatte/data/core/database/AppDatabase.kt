package com.segnities007.yatte.data.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.AutoMigrationSpec
import androidx.room.AutoMigration
import com.segnities007.yatte.data.core.database.dao.AlarmDao
import com.segnities007.yatte.data.core.database.dao.CategoryDao
import com.segnities007.yatte.data.core.database.dao.HistoryDao
import com.segnities007.yatte.data.core.database.dao.TaskDao
import com.segnities007.yatte.data.core.database.entity.AlarmEntity
import com.segnities007.yatte.data.core.database.entity.CategoryEntity
import com.segnities007.yatte.data.core.database.entity.HistoryEntity
import com.segnities007.yatte.data.core.database.entity.TaskEntity


@Database(
    entities = [
        TaskEntity::class,
        HistoryEntity::class,
        AlarmEntity::class,
        CategoryEntity::class,
    ],
    version = 6,
    autoMigrations = [
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
    ],
    exportSchema = true,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun historyDao(): HistoryDao
    abstract fun alarmDao(): AlarmDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "yatte.db"
    }
}


@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
