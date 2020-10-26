package com.example.bookmarkse_kotlin.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.graphics.convertTo
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.databinding.BookmarkItemBinding
import java.lang.IllegalStateException
import java.util.zip.Inflater

class BookmarkAdapter(
    private var mBookmarks: List<Bookmark>
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    init {
        setBookmarks(mBookmarks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = BookmarkItemBinding.inflate(inflater)

        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bindViewHolder(mBookmarks[position])
    }

    override fun getItemCount(): Int = mBookmarks.size

    private fun setBookmarks(bookmarks: List<Bookmark>) {
        mBookmarks = bookmarks
        notifyDataSetChanged()
    }

    fun replaceBookmarks(bookmarks: List<Bookmark>) = setBookmarks(bookmarks)

    class BookmarkViewHolder(private val viewBinding: BookmarkItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindViewHolder(bookmark: Bookmark) {
            viewBinding.bookmark = bookmark
            viewBinding.clBookmark.setOnClickListener {
                viewBinding.listener?.onBookmarkClicked(bookmark)
            }
        }
    }
}