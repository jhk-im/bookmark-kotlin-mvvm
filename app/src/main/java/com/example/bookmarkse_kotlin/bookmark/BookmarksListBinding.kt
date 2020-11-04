package com.example.bookmarkse_kotlin.bookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.deletebookmark.DeleteBookmarkAdapter

object BookmarksListBinding {

    @BindingAdapter("app:bookmarks")
    @JvmStatic
    fun setBookmarks(list: RecyclerView,@NonNull bookmarks: List<Bookmark>) {

        with(list.adapter as? BookmarkAdapter) {
            this?.replaceBookmarks(bookmarks)
        }
    }
}