package com.example.bookmarkse_kotlin.util

import androidx.databinding.BindingAdapter
import com.example.bookmarkse_kotlin.ScrollChildSwipeRefreshLayout
import com.example.bookmarkse_kotlin.home.HomeViewModel

@BindingAdapter("android:onRefresh")
fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
    viewModel: HomeViewModel ) {
    setOnRefreshListener { viewModel.loadBookmarks(true) }
}