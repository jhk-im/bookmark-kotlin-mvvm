package com.example.bookmarkse_kotlin.bookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.data.Bookmark

object BookmarksListBinding {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(list: RecyclerView,@NonNull items: List<Bookmark>) {

        with(list.adapter as? BookmarkAdapter) {
            this?.replaceBookmarks(items)
        }
    }
}