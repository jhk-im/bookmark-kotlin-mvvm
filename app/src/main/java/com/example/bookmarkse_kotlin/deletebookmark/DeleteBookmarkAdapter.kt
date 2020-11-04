package com.example.bookmarkse_kotlin.deletebookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.bookmark.BookmarkAdapter
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.databinding.BookmarkItemBinding
import com.example.bookmarkse_kotlin.databinding.DeleteBookmarkItemBinding

class DeleteBookmarkAdapter(
    private var bookmarks: List<Bookmark>,
    private var viewModel: DeleteBookmarkViewModel
) : RecyclerView.Adapter<DeleteBookmarkAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DeleteBookmarkItemBinding.inflate(inflater,parent,false)
        return DeleteBookmarkAdapter.BookmarkViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bindViewHolder(bookmarks[position], position)
    }

    override fun getItemCount(): Int = bookmarks.size

    private fun setBookmarks(bookmarks: List<Bookmark>) {
        this.bookmarks = bookmarks
        notifyDataSetChanged()
    }

    fun replaceBookmarks(bookmarks: List<Bookmark>) = setBookmarks(bookmarks)

    class BookmarkViewHolder(
        private val viewBinding: DeleteBookmarkItemBinding,
        private val viewModel: DeleteBookmarkViewModel
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindViewHolder(bookmark: Bookmark, position: Int) {
            with(viewBinding) {
                this.bookmark = bookmark
                Glide.with(viewBinding.root)
                    .load(bookmark.favicon)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(ivUrlImage)
                executePendingBindings()
            }

            viewBinding.clBookmark.setOnClickListener {

            }
        }
    }
}