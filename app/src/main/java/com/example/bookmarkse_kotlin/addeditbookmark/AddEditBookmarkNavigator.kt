package com.example.bookmarkse_kotlin.addeditbookmark

import com.example.bookmarkse_kotlin.data.Bookmark

interface AddEditBookmarkNavigator {

    fun onItemSaved(categoryId: String)
}