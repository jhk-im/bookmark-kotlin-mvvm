package com.example.bookmarkse_kotlin.addeditbookmark

import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.data.source.ItemsRepository


class AddEditBookmarkViewModel(
    private val bookmarkRepository: ItemsRepository
): ViewModel() {

}