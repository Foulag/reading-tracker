package com.foulag.readingtracker.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.foulag.readingtracker.data.BookRepository
import com.foulag.readingtracker.data.local.BookEntity
import com.foulag.readingtracker.data.local.ReadingStatus
import com.foulag.readingtracker.data.local.ReadingTrackerDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookRepository(
        ReadingTrackerDatabase.getInstance(application).bookDao()
    )

    val books = repository.observeBooks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun addBook(title: String, author: String, pageCount: Int?) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return

        viewModelScope.launch {
            repository.save(
                BookEntity(
                    title = cleanTitle,
                    author = author.trim(),
                    pageCount = pageCount,
                    status = ReadingStatus.TO_READ
                )
            )
        }
    }

    fun advancePage(book: BookEntity) {
        val maxPage = book.pageCount
        val nextPage = if (maxPage != null) {
            (book.currentPage + 1).coerceAtMost(maxPage)
        } else {
            book.currentPage + 1
        }
        val nextStatus = if (maxPage != null && nextPage >= maxPage) {
            ReadingStatus.READ
        } else if (book.status == ReadingStatus.TO_READ) {
            ReadingStatus.READING
        } else {
            book.status
        }
        viewModelScope.launch {
            repository.save(book.copy(currentPage = nextPage, status = nextStatus))
        }
    }

    fun updateStatus(book: BookEntity, status: ReadingStatus) {
        val page = if (status == ReadingStatus.READ) {
            book.pageCount ?: book.currentPage
        } else {
            book.currentPage
        }
        viewModelScope.launch {
            repository.save(book.copy(status = status, currentPage = page))
        }
    }

    fun deleteBook(bookId: Long) {
        viewModelScope.launch { repository.delete(bookId) }
    }
}
