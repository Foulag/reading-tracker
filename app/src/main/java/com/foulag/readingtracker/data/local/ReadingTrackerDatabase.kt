package com.foulag.readingtracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

class ReadingStatusConverter {
    @TypeConverter
    fun fromStatus(status: ReadingStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): ReadingStatus = ReadingStatus.valueOf(value)
}

@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ReadingStatusConverter::class)
abstract class ReadingTrackerDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: ReadingTrackerDatabase? = null

        fun getInstance(context: Context): ReadingTrackerDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReadingTrackerDatabase::class.java,
                    "reading_tracker.db"
                ).build().also { INSTANCE = it }
            }
    }
}
