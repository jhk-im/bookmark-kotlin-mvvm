package com.example.bookmarkse_kotlin.bookmarkdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import java.text.SimpleDateFormat
import java.util.ArrayList

class BookmarkDetailViewModel(
    private val itemRepository: ItemsRepository
) : ViewModel(), ItemsDataSource.GetBookmarkCallback {

    val categoryTitle = MutableLiveData<String>()

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

    val categoryId: String?
        get() = bookmark.value?.categoryId

    val selectedAt: String?
        @SuppressLint("SimpleDateFormat")
        get() {
            val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            return sd.format(bookmark.value?.selectedAt)
        }

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
        onCategoriesLoaded()
    }

    override fun onDataNotAvailable() {
        _bookmark.value = null
        _isDataAvailable.value = false
    }

    private fun onCategoriesLoaded() {
        itemRepository.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {

                for (category in categories) {
                    if (categoryId == category.id)
                        categoryTitle.value = category.title
                }
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }
        })
    }

    fun onRefresh() {
        bookmarkId?.let { start(it) }
    }
}