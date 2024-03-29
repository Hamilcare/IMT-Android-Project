package club.barnab2.quiedeville.myapplication

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import club.barnab2.quiedeville.myapplication.entity.Book
import club.barnab2.quiedeville.myapplication.fragments.BookDetailFragment
import club.barnab2.quiedeville.myapplication.fragments.BookListFragment
import club.barnab2.quiedeville.myapplication.service.HenryPotierService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnBookSelected {

    private val newBookActivityRequestCode = 1
    private lateinit var bookViewModel: BookViewModel
    private lateinit var bookService: HenryPotierService
    private var isLandscape = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.root_layout, BookListFragment.newInstance(), "bookList")
//                .commit()
//        }

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.landscape_list, BookListFragment.newInstance(), "bookList")
                .replace(R.id.landscape_detail, BookDetailFragment.newInstance(), "detail").commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, BookListFragment.newInstance(), "bookList")
                .addToBackStack(null)
                .commit()
        }



        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        setupAddButton()
        setupDeleteAllButton()
        setupSyncButton()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(HenryPotierService::class.java)

    }

    override fun onBookSelected(book: Book) {
        Toast.makeText(
            this, "Hey, you selected " + book.title + "!",
            Toast.LENGTH_SHORT
        ).show()

        val detailFragment = BookDetailFragment.newInstance(book)

        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.landscape_detail, detailFragment, "detail").addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.root_layout, detailFragment, "detail").addToBackStack(null)
                .commit()
        }


    }


    private fun setupAddButton() {
        val fab = findViewById<FloatingActionButton>(R.id.add_book)
        fab.setOnClickListener {
            val intent = Intent(this, NewBookActivity::class.java)
            startActivityForResult(intent, newBookActivityRequestCode)
        }
    }

    private fun setupDeleteAllButton() {
        val deleteAll = findViewById<FloatingActionButton>(R.id.drop_table)
        deleteAll.setOnClickListener {
            Toast.makeText(applicationContext, "Deleting data", Toast.LENGTH_SHORT).show()
            bookViewModel.deleteAll()
        }
    }

    private fun setupSyncButton() {
        val syncButton = findViewById<FloatingActionButton>(R.id.force_sync)
        syncButton.setOnClickListener {
            Toast.makeText(applicationContext, "Synchronizing", Toast.LENGTH_SHORT).show()
            syncBook()
        }
    }

    //
    private fun syncBook() {
        val lstBooks = bookService.getAllBooks()

        lstBooks.enqueue(object : Callback<List<Book>> {

            //On Success
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                val allBooks = response.body()
                allBooks?.let {
                    for (book in it) {
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

        if (requestCode == newBookActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<Book>(NewBookActivity.EXTRA_REPLY)?.let {

                bookViewModel.insert(it)
            }
        } else {
            Toast.makeText(applicationContext, "Something wrong appened", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val url = "http://henri-potier.xebia.fr/"
    }
}
