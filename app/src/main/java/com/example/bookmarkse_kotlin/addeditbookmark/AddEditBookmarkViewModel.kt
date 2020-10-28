package com.example.bookmarkse_kotlin.addeditbookmark

import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository


class AddEditBookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {

}