package club.barnab2.vq.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.barnab2.vq.myapplication.BookListAdapter
import club.barnab2.vq.myapplication.BookViewModel
import club.barnab2.vq.myapplication.OnBookSelected
import club.barnab2.vq.myapplication.R
import club.barnab2.vq.myapplication.entity.Book
import club.barnab2.vq.myapplication.service.HenryPotierService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ClassCastException

class BookListFragment : Fragment() {

    private lateinit var bookViewModel: BookViewModel
    private lateinit var bookService: HenryPotierService
    lateinit var listener : OnBookSelected

    companion object {
        fun newInstance(): BookListFragment {
            return BookListFragment()
        }

        const val url = "http://henri-potier.xebia.fr/"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        if(context is OnBookSelected) {
            listener = context
        } else {
            throw ClassCastException(context.toString()+" must iplment onBookSelected.")
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(HenryPotierService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_book_list, container,
            false
        )
        val activity = activity as Context
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        val adapter = BookListAdapter(activity,listener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        bookViewModel.allBooks.observe(viewLifecycleOwner, Observer { books -> books?.let {adapter.setBooks(it)} })

        return view
    }

}

