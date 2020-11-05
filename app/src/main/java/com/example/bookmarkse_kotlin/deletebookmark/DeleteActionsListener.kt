package com.example.bookmarkse_kotlin.deletebookmark

import com.example.bookmarkse_kotlin.data.Bookmark

interface DeleteActionsListener {

    fun onBookmarkClicked(bookmark: Bookmark)
}