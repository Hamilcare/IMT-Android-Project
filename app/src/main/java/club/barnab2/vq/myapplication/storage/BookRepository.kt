package club.barnab2.vq.myapplication.storage

import androidx.lifecycle.LiveData
import club.barnab2.vq.myapplication.entity.Book

class BookRepository(private val bookDao: BookDao) {

    val allBooks: LiveData<List<Book>> = bookDao.getAllBooks()

    suspend fun insert(book: Book) {
        bookDao.insert(book)
    }

    suspend fun deleteAll() {
        bookDao.deleteAll()
    }
}