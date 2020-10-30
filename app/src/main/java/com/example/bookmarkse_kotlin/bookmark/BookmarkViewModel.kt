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
import com.example.bookmarkse_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import com.example.bookmarkse_kotlin.util.ADD_EDIT_RESULT_OK

class BookmarkViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _bookmarks = MutableLiveData<List<Bookmark>>().apply { value = emptyList() }
    val bookmarks: LiveData<List<Bookmark>>
        get() = _bookmarks

    private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _currentCategory = MutableLiveData<String>()
    val currentCategory: LiveData<String>
        get() = _currentCategory

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

//    private val _openBookmarkEvent = MutableLiveData<Event<String>>()
//    val openBookmarkEvent: LiveData<Event<String>>
//        get() = _openBookmarkEvent

    private val _newBookmarkEvent = MutableLiveData<Event<Unit>>()
    val newBookmarkEvent: LiveData<Event<Unit>>
        get() = _newBookmarkEvent

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var currentFiltering = BookmarkFilterType.RECENT_BOOKMARKS

    val empty: LiveData<Boolean> = Transformations.map(_bookmarks) {
        it.isEmpty()
    }

    init {
        setFiltering(BookmarkFilterType.RECENT_BOOKMARKS)
         //itemsRepository.deleteAllItems()
         //testLocalDatabase()
    }

    fun start() {
        loadItems(false)
    }

    fun loadItems(forceUpdate: Boolean) {
        loadItems(forceUpdate, true)
    }

    fun setFiltering(requestType: BookmarkFilterType) {
        currentFiltering = requestType

        when (requestType) {
            BookmarkFilterType.RECENT_BOOKMARKS -> {
                setFilter(
                    R.string.label_recent,
                    R.string.no_bookmarks,
                    R.drawable.ic_bookmark_grey,
                    true
                )
            }
            BookmarkFilterType.CATEGORY_BOOKMARKS -> {
                setFilter(
                    R.string.label_category,
                    R.string.no_bookmarks,
                    R.drawable.ic_bookmark_grey,
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

    internal fun clickedCategory(categoryId: String){

    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == AddEditBookmarkActivity.REQUEST_CODE) {
            when (resultCode) {
                ADD_EDIT_RESULT_OK -> {
                    _snackbarText.value = Event(R.string.successfully_saved_message)
                    start()
                }
            }
        }
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

    private fun loadItems(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            _dataLoading.value = true
        }

        if (forceUpdate) {
            itemsRepository.refreshBookmark()
        }

        itemsRepository.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {

                val bookmarksToShow = ArrayList<Bookmark>()
                val categoriesToShow = ArrayList<Category>()
                //val sd = SimpleDateFormat("HH:mm:ss.SSS")

                for (category in categories) {
                    if (categories.indexOf(category) == 0) {
                        _currentCategory.value = category.id
                    }
                    categoriesToShow.add(category)
                }

                for (bookmark in bookmarks) {
                    when (currentFiltering) {
                        BookmarkFilterType.RECENT_BOOKMARKS -> {
                            bookmarksToShow.add(bookmark)
                            categoriesToShow.clear()
                        }
                        BookmarkFilterType.CATEGORY_BOOKMARKS -> {
                            if (bookmark.categoryId == currentCategory.value)
                                bookmarksToShow.add(bookmark)
                        }
                    }
                }

                if (showLoadingUI) {
                    _dataLoading.value = false
                }
                isDataLoadingError.value = false

                val bookmarksValue = ArrayList(bookmarksToShow)
                _bookmarks.value = bookmarksValue
                val categoriesValue = ArrayList(categoriesToShow)
                _categories.value = categoriesValue
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.value = true
                _dataLoading.value = false
            }
        })
    }


    private fun testLocalDatabase() {

        val newBookmark = Bookmark("Google", "https://www.google.com").apply {
            favicon = "https://www.google.com/favicon.ico"
        }
        val newCategory = Category("One")
        itemsRepository.saveCategory(newCategory)
        itemsRepository.saveBookmark(newCategory.title, newBookmark)

        val newBookmark2 = Bookmark("Naver", "https://www.naver.com").apply {
            favicon = "https://www.naver.com/favicon.ico"
        }
        val newCategory2 = Category("Two")
        itemsRepository.saveCategory(newCategory2)
        itemsRepository.saveBookmark(newCategory2.title, newBookmark2)

        val newBookmark3 = Bookmark("Daum", "https://www.daum.net").apply {
            favicon = "https://www.daum.net/favicon.ico"
        }
        val newCategory3 = Category("Three")
        itemsRepository.saveCategory(newCategory3)
        itemsRepository.saveBookmark(newCategory3.title, newBookmark3)

        val newBookmark4 = Bookmark("Nate", "https://www.nate.com").apply {
            favicon = "https://www.nate.com/favicon.ico"
        }
        val newCategory4 = Category("One")
        itemsRepository.saveCategory(newCategory4)
        itemsRepository.saveBookmark(newCategory4.title, newBookmark4)


    }
}