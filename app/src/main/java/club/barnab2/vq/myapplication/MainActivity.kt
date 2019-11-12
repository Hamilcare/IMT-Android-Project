package club.barnab2.vq.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.barnab2.vq.myapplication.entity.Book
import club.barnab2.vq.myapplication.service.HenryPotierService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private  val newBookActivityRequestCode = 1
    private lateinit var bookViewModel: BookViewModel
    private lateinit var bookService : HenryPotierService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = BookListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        bookViewModel= ViewModelProvider(this).get(BookViewModel::class.java)
        bookViewModel.allBooks.observe(this, Observer { books -> books?.let {adapter.setBooks(it)} })

        Log.d("Main_activity","Main_activity")



        setupAddButton()
        setupDeleteAllButton()
        setupSyncButton()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(HenryPotierService::class.java)
        syncBook()

    }

    private fun setupAddButton() {
        val fab = findViewById<FloatingActionButton>(R.id.add_book)
        fab.setOnClickListener {
            val intent = Intent(this, NewBookActivity::class.java)
            startActivityForResult(intent, newBookActivityRequestCode)
        }
    }

    private fun setupDeleteAllButton(){
        val deleteAll = findViewById<FloatingActionButton>(R.id.drop_table)
        deleteAll.setOnClickListener {
            Toast.makeText(applicationContext,"Deleting data",Toast.LENGTH_SHORT).show()
            bookViewModel.deleteAll()
        }
    }

    private fun setupSyncButton(){
        val syncButton =findViewById<FloatingActionButton>(R.id.force_sync)
        syncButton.setOnClickListener {
            Toast.makeText(applicationContext,"Synchronizing",Toast.LENGTH_SHORT).show()
            syncBook()
        }
    }

    private fun syncBook(){
        val lstBooks = bookService.getAllBooks()

        lstBooks.enqueue(object :Callback<List<Book>>{

            //On Success
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                val allBooks = response.body()
                allBooks?.let{
                    for (book in it){
                        Log.d("BOOK", "book : ${book.title}")
                        bookViewModel.insert(book)
                    }
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.e("ERROR RECUP BOOK", "Error $t")
            }


        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newBookActivityRequestCode && resultCode == Activity.RESULT_OK){
            data?.getStringExtra(NewBookActivity.EXTRA_REPLY)?.let {
                val newBook = Book(
                    it,
                    "title",
                    12.5f,
                    "cover",
                    arrayOf("1", "2", "3")
                )
                bookViewModel.insert(newBook)
            }
        } else{
            Toast.makeText(applicationContext,"Something wrong appened",Toast.LENGTH_LONG).show()
        }
    }

    companion object{
        const val url = "http://henri-potier.xebia.fr/"
    }
}
