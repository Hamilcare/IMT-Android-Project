package club.barnab2.quiedeville.myapplication.service


import club.barnab2.quiedeville.myapplication.entity.Book
import retrofit2.Call
import retrofit2.http.GET

interface HenryPotierService {

    // TODO Method GET books which return a List<Book>

    @GET("books")
    fun getAllBooks(): Call<List<Book>>

}