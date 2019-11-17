package club.barnab2.vq.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import club.barnab2.vq.myapplication.R

class BookDetailFragment : Fragment(){

    companion object {
        fun newInstance(): BookDetailFragment {
            return BookDetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }
}