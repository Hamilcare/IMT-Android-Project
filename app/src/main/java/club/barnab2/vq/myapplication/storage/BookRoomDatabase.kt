package club.barnab2.vq.myapplication.storage

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import club.barnab2.vq.myapplication.entity.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Book::class), version = 1, exportSchema = false)
public abstract class BookRoomDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {

        // une seule connexion à la db
        @Volatile
        private var DB_INSTANCE: BookRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BookRoomDatabase {
            val tempInstance =
                DB_INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookRoomDatabase::class.java,
                    "book_database"
                )
//                    .addCallback(
//                    BookDatabaseCallback(
//                        scope
//                    )
//                )
                    .build()
                DB_INSTANCE = instance
                return instance
            }
        }
    }

    private class BookDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            DB_INSTANCE?.let { dbInstance ->
                scope.launch {
                    populateDatabase(dbInstance.bookDao())
                }
            }
        }

        suspend fun populateDatabase(bookDao: BookDao) {
            Log.d("BOOK", "Book : coucou")
            var book = Book(
                "isbn",
                "title",
                12.5f,
                "cover",
                arrayOf("1", "2", "3")
            )
            bookDao.insert(book)
        }
    }
}

