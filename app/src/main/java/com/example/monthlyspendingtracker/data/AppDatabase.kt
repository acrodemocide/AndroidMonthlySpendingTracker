package com.example.monthlyspendingtracker.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 2,
    entities = [ExpenseEntity::class],
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `expenses` ADD COLUMN 'description' VARCHAR(250)")
    }
}

var database: AppDatabase? = null
@Composable
fun GetDatabase(): AppDatabase {
    if (database != null) return database!!
    else {
        database = Room.databaseBuilder(
            LocalContext.current,
            AppDatabase::class.java,
            "expenses-db"
            )
            .addMigrations(MIGRATION_1_2)
            .build()
        return database!!
    }
}
