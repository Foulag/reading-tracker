package com.foulag.readingtracker.data.local

import androidx.room.Database
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
}
