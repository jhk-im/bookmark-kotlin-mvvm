package com.example.bookmarkse_kotlin.bookmark

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Bookmark>>().apply { value = emptyList() }
    val items: LiveData<List<Bookmark>>
        get() = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int>
        get() = _currentFilteringLabel

    private val _noBookmarkLabel = MutableLiveData<Int>()
    val noBookmarkLabel: LiveData<Int>
        get() = _noBookmarkLabel

    private val _noBookmarkIconRes = MutableLiveData<Int>()
    val noBookmarkIconRes: LiveData<Int>
        get() = _noBookmarkIconRes

    private val _bookmarksAddViewVisible = MutableLiveData<Boolean>()
    val bookmarksAddViewVisible: LiveData<Boolean>
        get() = _bookmarksAddViewVisible

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private val _openBookmarkEvent = MutableLiveData<Event<String>>()
    val openBookmarkEvent: LiveData<Event<String>>
        get() = _openBookmarkEvent

    private val _newBookmarkEvent = MutableLiveData<Event<Unit>>()
    val newBookmarkEvent: LiveData<Event<Unit>>
        get() = _newBookmarkEvent

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var currentFiltering = BookmarkFilterType.RECENT_BOOKMARKS

    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    init {
        setFiltering(BookmarkFilterType.RECENT_BOOKMARKS)
        // bookmarkRepository.deleteAllBookmarks()
        // testLocalDatabase()
    }

    fun start() {
        loadBookmarks(false)
    }

    fun loadBookmarks(forceUpdate: Boolean) {
        loadBookmarks(forceUpdate, true)
    }

    fun setFiltering(requestType: BookmarkFilterType) {
        currentFiltering = requestType

        when (requestType) {
            BookmarkFilterType.RECENT_BOOKMARKS -> {
                setFilter(
                    R.string.label_recent,
                    R.string.no_bookmarks,
                    R.drawable.ic_assignment_turned_in_24dp,
                    true
                )
            }
            BookmarkFilterType.CATEGORY_BOOKMARKS -> {
                setFilter(
                    R.string.label_category,
                    R.string.no_bookmarks,
                    R.drawable.ic_assignment_turned_in_24dp,
                    false
                )
            }
        }
    }

    fun addNewBookmarks() {
        _newBookmarkEvent.value = Event(Unit)
    }

    internal fun openBookmark(bookmarkId: String) {
        //_openBookmarkEvent.value = Event(bookmarkId)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {
        // AddEditBookmarkActivity ..
    }

    private fun showSnackbarMessage(message: Int) {
        //_snackbarText.value = Event(message)
    }

    private fun setFilter(
        @StringRes filteringLabelString: Int,
        @StringRes noBookmarkLabelString: Int,
        @DrawableRes noBookmarkIconDrawable: Int,
        bookmarkAddVisible: Boolean
    ) {
        _currentFilteringLabel.value = filteringLabelString
        _noBookmarkLabel.value = noBookmarkLabelString
        _noBookmarkIconRes.value = noBookmarkIconDrawable
        _bookmarksAddViewVisible.value = bookmarkAddVisible
    }

    private fun loadBookmarks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            _dataLoading.setValue(true)
        }

        if (forceUpdate) {
            bookmarkRepository.refreshBookmark()
        }

        bookmarkRepository.getBookmarks(object : BookmarkDataSource.LoadBookmarksCallback {
            override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                val bookmarksToShow = ArrayList<Bookmark>()
                val sd = SimpleDateFormat("HH:mm:ss.SSS")
                for (bookmark in bookmarks) {
                    when (currentFiltering) {
                        BookmarkFilterType.RECENT_BOOKMARKS -> bookmarksToShow.add(bookmark)
                        BookmarkFilterType.CATEGORY_BOOKMARKS -> bookmarksToShow.add(bookmark)

                    }
                    Log.d(
                        "bookmark",
                        bookmark.id + "\n" +
                                bookmark.title + "\n" +
                                bookmark.url + "\n" +
                                sd.format(bookmark.selectedAt) + "\n" +
                                bookmark.categoryId
                    )
                }

                if (showLoadingUI) {
                    _dataLoading.value = false
                }
                isDataLoadingError.value = false

                val itemsValue = ArrayList(bookmarksToShow)
                _items.value = itemsValue
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.value = true
            }

        })
    }


    private fun testLocalDatabase() {

        val selectedAt = Date()
        // val newCategory = Category("Portal")
        val newBookmark =
            Bookmark("Google", "https://www.google.com", selectedAt)
        bookmarkRepository.saveBookmark("One", newBookmark)

        val selectedAt2 = Date()
        // val newCategory2 = Category("Portal")
        val newBookmark2 =
            Bookmark("Naver", "https://www.naver.com", selectedAt2)
        bookmarkRepository.saveBookmark("Two", newBookmark2)

        val selectedAt3 = Date()
        // val newCategory3 = Category("Portal")
        val newBookmark3 =
            Bookmark("Daum", "https://www.daum.com", selectedAt3)
        bookmarkRepository.saveBookmark("Three", newBookmark3)

        // val sd = SimpleDateFormat("HH:mm:ss.SSS")

    }
}