package com.foulag.readingtracker.data

import com.foulag.readingtracker.data.local.BookDao
import com.foulag.readingtracker.data.local.BookEntity
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    fun observeBooks(): Flow<List<BookEntity>> = bookDao.observeAll()

    suspend fun save(book: BookEntity) {
        bookDao.upsert(book)
    }

    suspend fun delete(bookId: Long) {
        bookDao.delete(bookId)
    }
}
