package com.example.bookmarkse_kotlin.bookmark

import com.example.bookmarkse_kotlin.data.Bookmark

interface BookmarkUserActionsListener {
    fun onBookmarkClicked(bookmark: Bookmark)
}