/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jroomdev.bookmark_kotlin.bookmark

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jroomdev.bookmark_kotlin.Event
import com.jroomdev.bookmark_kotlin.R
import com.jroomdev.bookmark_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.jroomdev.bookmark_kotlin.data.Bookmark
import com.jroomdev.bookmark_kotlin.data.Category
import com.jroomdev.bookmark_kotlin.data.source.ItemsDataSource
import com.jroomdev.bookmark_kotlin.data.source.ItemsRepository
import com.jroomdev.bookmark_kotlin.util.ADD_EDIT_RESULT_OK

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

    private val _isCategoriesSetup = MutableLiveData<Boolean>()
    val isCategoriesSetup: LiveData<Boolean>
        get() = _isCategoriesSetup

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private val _bookmarkTitle = MutableLiveData<TextView>()
    val bookmarkTitle: LiveData<TextView>
        get() = _bookmarkTitle

    private val _bookmarkUrl = MutableLiveData<TextView>()
    val bookmarkUrl: LiveData<TextView>
        get() = _bookmarkUrl

    private val _bookmarkImage = MutableLiveData<ImageView>()
    val bookmarkImage: LiveData<ImageView>
        get() = _bookmarkImage

    private val _openBookmarkEvent = MutableLiveData<Event<String>>()
    val openBookmarkEvent: LiveData<Event<String>>
        get() = _openBookmarkEvent

    private val _newBookmarkEvent = MutableLiveData<Event<Unit>>()
    val newBookmarkEvent: LiveData<Event<Unit>>
        get() = _newBookmarkEvent

    private val _deleteBookmarkEvent = MutableLiveData<Event<Unit>>()
    val deleteBookmarkEvent: LiveData<Event<Unit>>
        get() = _deleteBookmarkEvent

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var currentFiltering = BookmarkFilterType.CATEGORY_BOOKMARKS

    val empty: LiveData<Boolean> = Transformations.map(_bookmarks) {
        it.isEmpty()
    }

    init {
        setFiltering(BookmarkFilterType.CATEGORY_BOOKMARKS)
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

    fun deleteBookmarks() {
        _deleteBookmarkEvent.value = Event(Unit)
    }

    internal fun openBookmark(
        bookmarkId: String,
        image: ImageView,
        title: TextView,
        url: TextView
    ) {
        _bookmarkImage.value = image
        _bookmarkTitle.value = title
        _bookmarkUrl.value = url
        _openBookmarkEvent.value = Event(bookmarkId)
    }

    internal fun clickedCategory(categoryId: String) {
        _currentCategory.value = categoryId
        _isCategoriesSetup.value = false
        loadItems(false, showLoadingUI = false)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, categoryId: String?) {
        if (requestCode == AddEditBookmarkActivity.REQUEST_CODE) {
            when (resultCode) {
                ADD_EDIT_RESULT_OK -> {
                    setFiltering(BookmarkFilterType.CATEGORY_BOOKMARKS)
                    _currentCategory.value = categoryId
                    _snackbarText.value = Event(R.string.successfully_saved_message)
                    loadItems(false)
                    // Log.e("handle", "$categoryId")
                }
            }
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
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

                for (category in categories) {
                    if (_currentCategory.value == null && categories.indexOf(category) == 0)
                        _currentCategory.value = category.id

                    var isBookmarkEmpty = true
                    for (bookmark in bookmarks){
                        if(bookmark.categoryId == category.id)
                            isBookmarkEmpty = false
                    }
                    if (isBookmarkEmpty){
                        itemsRepository.deleteCategory(category.id)
                    } else {
                        categoriesToShow.add(category)
                    }
                }

                for (bookmark in bookmarks) {
                    when (currentFiltering) {
                        BookmarkFilterType.RECENT_BOOKMARKS -> {
                            bookmarksToShow.add(bookmark)
                            _currentCategory.value = null
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

                _isCategoriesSetup.value = true
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.value = true
                _dataLoading.value = false
            }
        })
    }
}