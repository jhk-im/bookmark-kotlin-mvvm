package com.example.bookmarkse_kotlin.bookmark

import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category

interface ItemUserActionsListener {

    fun onBookmarkClicked(bookmark: Bookmark)
    fun onCategoryClicked(category: Category)
}