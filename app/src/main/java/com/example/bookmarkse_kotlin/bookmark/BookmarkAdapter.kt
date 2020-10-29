package com.example.bookmarkse_kotlin.bookmark

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.core.graphics.convertTo
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.databinding.BookmarkItemBinding
import java.lang.IllegalStateException
import java.util.zip.Inflater

class BookmarkAdapter(
    private var mBookmarks: List<Bookmark>,
    private var viewModel: BookmarkViewModel
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = BookmarkItemBinding.inflate(inflater,parent,false)
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
            with(viewBinding){
                this.bookmark = bookmark
                Glide.with(viewBinding.root)
                    .load(bookmark.favicon)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(ivUrlImage)
                executePendingBindings()
            }

//            viewBinding.clBookmark.setOnClickListener {
//                viewBinding.listener?.onBookmarkClicked(bookmark)
//            }
        }
    }
}