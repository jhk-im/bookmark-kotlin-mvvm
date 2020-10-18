package com.example.bookmarkse_kotlin.home

import android.view.View
import com.example.bookmarkse_kotlin.data.Bookmark

interface BookmarkUserActionsListener {
    fun onBookmarkClicked(bookmark: Bookmark)
}