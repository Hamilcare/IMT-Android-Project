package club.barnab2.quiedeville.myapplication.storage

import androidx.room.TypeConverter

public class BookSynopsisConverter {

    @TypeConverter
    fun fromList(synopsis: Array<String>): String {
        return synopsis.joinToString()
    }

    @TypeConverter
    fun fromString(synopsis: String): Array<String> {
        val myList = synopsis.split(";").toTypedArray()
        return myList
    }

}