package com.example.bookmarkse_kotlin.bookmarkdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository

class BookmarkDetailViewModel(
    private val itemRepository: ItemsRepository
) : ViewModel(), ItemsDataSource.GetBookmarkCallback {

    private val _bookmark = MutableLiveData<Bookmark>()
    val bookmark: LiveData<Bookmark>
        get() = _bookmark

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean>
        get() = _isDataAvailable

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarMessage: LiveData<Event<Int>>
        get() = _snackBarText

    val bookmarkId: String?
        get() = bookmark.value?.id

    fun editBookmark() {

    }

    fun openWeb() {

    }

    fun start(bookmarkId: String?) {
        if (bookmarkId != null) {
            itemRepository.getBookmark(bookmarkId, this)
        }
    }

    private fun setBookmark(bookmark: Bookmark?) {
        this._bookmark.value = bookmark
        _isDataAvailable.value = bookmark != null
    }

    override fun onBookmarkLoaded(bookmark: Bookmark) {
        setBookmark(bookmark)
    }

    override fun onDataNotAvailable() {
        _bookmark.value = null
        _isDataAvailable.value = false
    }

    fun onRefresh() {
        bookmarkId?.let { start(it) }
    }
}