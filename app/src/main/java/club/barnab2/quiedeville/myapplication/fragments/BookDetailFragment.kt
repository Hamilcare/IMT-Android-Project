package club.barnab2.quiedeville.myapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import club.barnab2.quiedeville.myapplication.R
import club.barnab2.quiedeville.myapplication.entity.Book
import com.squareup.picasso.Picasso

class BookDetailFragment : Fragment() {

    private lateinit var picassoBuilder: Picasso.Builder
    private lateinit var picasso: Picasso

    companion object {
        fun newInstance(book: Book): BookDetailFragment {
            val args = Bundle()
            args.putParcelable("selectedBook", book)
            val fragment = BookDetailFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): BookDetailFragment {
            return BookDetailFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        picassoBuilder = Picasso.Builder(context!!)
        picasso = picassoBuilder.listener(object : Picasso.Listener {
            override fun onImageLoadFailed(picasso: Picasso, uri: Uri, exception: Exception) {
                Log.d("error Picasso", "exception $exception")
            }
        }).build()

        val view = inflater.inflate(R.layout.fragment_book_details, container, false)
        val book: Book?

        if (arguments != null && arguments!!.getParcelable<Book>("selectedBook") != null) {
            book = arguments!!.getParcelable<Book>("selectedBook")



            view.findViewById<TextView>(R.id.detail_title).text = book!!.title

            val coverView = view.findViewById<ImageView>(R.id.detail_view)
            picasso.load(book.cover).resize(343, 500).onlyScaleDown()
                .into(coverView)

            view.findViewById<TextView>(R.id.detail_synopsis).text = book!!.synopsis.joinToString()
        }
        return view

    }
}