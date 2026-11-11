package com.foulag.readingtracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String = "",
    val pageCount: Int? = null,
    val currentPage: Int = 0,
    val status: ReadingStatus = ReadingStatus.TO_READ,
    val createdAtEpochMillis: Long = System.currentTimeMillis()
) {
    val progressPercent: Int
        get() = if (pageCount == null || pageCount <= 0) 0
        else ((currentPage.coerceIn(0, pageCount) * 100f) / pageCount).toInt()
}
enum class ReadingStatus { TO_READ, READING, READ, PAUSED, ABANDONED }
