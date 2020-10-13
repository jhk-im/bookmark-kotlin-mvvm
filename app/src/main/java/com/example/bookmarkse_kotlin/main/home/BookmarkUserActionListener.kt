package com.example.bookmarkse_kotlin.main.home

import android.view.View
import com.example.bookmarkse_kotlin.data.Bookmark

interface BookmarkUserActionListener {
    fun onCompleteChanged(bookmark: Bookmark, v: View)
    fun onBookmarkClicked(bookmark: Bookmark)
}
