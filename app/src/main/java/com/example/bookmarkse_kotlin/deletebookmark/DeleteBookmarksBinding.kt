package com.example.bookmarkse_kotlin.deletebookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.bookmark.BookmarkAdapter
import com.example.bookmarkse_kotlin.data.Bookmark

object DeleteBookmarksBinding {

    @BindingAdapter("app:deleteBookmarks")
    @JvmStatic
    fun setDeleteBookmarks(list: RecyclerView, @NonNull bookmarks: List<Bookmark>) {
        with(list.adapter as? DeleteBookmarkAdapter) {
            this?.replaceBookmarks(bookmarks)
        }
    }
}