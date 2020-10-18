package com.example.bookmarkse_kotlin.home

import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.example.bookmarkse_kotlin.data.Bookmark

object BookmarksListBinding {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items: List<Bookmark>) {
        with(listView.adapter as BookmarkAdapter) {
            replaceData(items)
        }
    }
}