package club.barnab2.vq.myapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import club.barnab2.vq.myapplication.storage.BookSynopsisConverter

@Entity(tableName = "book_table")
@TypeConverters(BookSynopsisConverter::class)
data class Book (@PrimaryKey val isbn : String,
                 val title : String,
                 val price : Float,
                 val cover : String,
                 val synopsis : Array<String> ) {

    override fun equals(other: Any?): Boolean {
        val autre = other as Book
        return isbn == (autre.isbn)
    }

}