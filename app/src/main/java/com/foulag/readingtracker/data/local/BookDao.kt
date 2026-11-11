package com.foulag.readingtracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY createdAtEpochMillis DESC")
    fun observeAll(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE status = :status ORDER BY createdAtEpochMillis DESC")
    fun observeByStatus(status: ReadingStatus): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(book: BookEntity): Long

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun delete(bookId: Long)
}
