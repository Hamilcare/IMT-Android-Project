package club.barnab2.quiedeville.myapplication

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import club.barnab2.quiedeville.myapplication.entity.Book
import com.squareup.picasso.Picasso


class BookListAdapter internal constructor(
    context: Context,
    listener: OnBookSelected
) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var books = emptyList<Book>()
    private val listener = listener


    private val picassoBuilder = Picasso.Builder(context)
    private val picasso = picassoBuilder.listener(object : Picasso.Listener {
        override fun onImageLoadFailed(picasso: Picasso, uri: Uri, exception: Exception) {
            Log.d("error Picasso", "exception $exception")
        }
    }).build()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitleView: TextView = itemView.findViewById(R.id.titleView)
        val bookAuthorView: TextView = itemView.findViewById(R.id.authorView)
        val coverView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return BookViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = books[position]
//        val picasso = Picasso.get()


        holder.bookTitleView.text = current.title
        holder.bookAuthorView.text = current.getFormatedPrice()
        Log.d("BookItem", "Book : $current")
        picasso.load(current.cover).resize(343, 500).onlyScaleDown()
            .into(holder.coverView)

        holder.itemView.setOnClickListener {
            listener.onBookSelected(current)
        }
    }

    internal fun setBooks(words: List<Book>) {
        this.books = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = books.size


}