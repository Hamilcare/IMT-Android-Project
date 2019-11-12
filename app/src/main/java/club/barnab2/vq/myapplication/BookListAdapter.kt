package club.barnab2.vq.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import club.barnab2.vq.myapplication.entity.Book


class BookListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var books = emptyList<Book>()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return BookViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val current = books[position]
        holder.bookItemView.text = current.title
    }

    internal fun setBooks(words: List<Book>) {
        this.books = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = books.size


}