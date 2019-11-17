package club.barnab2.vq.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import club.barnab2.vq.myapplication.entity.Book
import club.barnab2.vq.myapplication.storage.BookRepository
import club.barnab2.vq.myapplication.storage.BookRoomDatabase
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookRepository

    val allBooks: LiveData<List<Book>>

    init {
        val bookDao = BookRoomDatabase.getDatabase(application, viewModelScope).bookDao()
        repository = BookRepository(bookDao)
        allBooks = repository.allBooks
    }

    fun insert(book: Book) = viewModelScope.launch {
        repository.insert(book)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }


}