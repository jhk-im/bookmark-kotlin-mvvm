package com.example.bookmarkse_kotlin.util

import androidx.databinding.BindingAdapter
import com.example.bookmarkse_kotlin.ScrollChildSwipeRefreshLayout
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel

@BindingAdapter("android:onRefresh")
fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
    viewModel: BookmarkViewModel ) {
    setOnRefreshListener { viewModel.loadBookmarks(true) }
}