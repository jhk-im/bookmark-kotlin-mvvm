package com.example.bookmarkse_kotlin.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.databinding.BookmarkItemBinding

class BookmarkAdapter(
    private var bookmarks: List<Bookmark>,
    private var viewModel: BookmarkViewModel
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkAdapter.BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = BookmarkItemBinding.inflate(inflater,parent,false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bindViewHolder(bookmarks[position])
    }

    override fun getItemCount(): Int = bookmarks.size

    private fun setBookmarks(bookmarks: List<Bookmark>) {
        this.bookmarks = bookmarks
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