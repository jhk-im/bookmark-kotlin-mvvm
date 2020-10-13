package com.example.bookmarkse_kotlin.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository

class HomeViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Bookmark>>().apply { value = emptyList() }
    val items: LiveData<List<Bookmark>> get() = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> get() = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun start() {
        loadBookmarks(false)
    }

    fun loadBookmarks(forceUpdate: Boolean) {
        loadBookmarks(forceUpdate, true)
    }

    private fun loadBookmarks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            _dataLoading.value = true
        }
        if (forceUpdate) {
            // refresh
        }

        bookmarkRepository.getBookmarks(object : BookmarkDataSource.LoadBookmarksCallback {
            override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                val bookmarksToShow = ArrayList<Bookmark>()

                for (bookmark in bookmarks) {
                    bookmarksToShow.add(bookmark)
                }
                if (showLoadingUI) {
                    _dataLoading.value = false
                }
                isDataLoadingError.value = false

                val itemValue = ArrayList(bookmarksToShow)
                _items.value = itemValue
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.value = true
            }
        })
    }
}
