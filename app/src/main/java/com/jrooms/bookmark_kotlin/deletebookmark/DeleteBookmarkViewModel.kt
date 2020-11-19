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
package com.jrooms.bookmark_kotlin.deletebookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jrooms.bookmark_kotlin.Event
import com.jrooms.bookmark_kotlin.R
import com.jrooms.bookmark_kotlin.data.Bookmark
import com.jrooms.bookmark_kotlin.data.Category
import com.jrooms.bookmark_kotlin.data.source.ItemsDataSource
import com.jrooms.bookmark_kotlin.data.source.ItemsRepository

class DeleteBookmarkViewModel(
  private val itemsRepository: ItemsRepository
) : ViewModel() {

  private val _bookmarks = MutableLiveData<List<Bookmark>>().apply { value = emptyList() }
  val bookmarks: LiveData<List<Bookmark>>
    get() = _bookmarks

  //    private val _deleteBookmarks = MutableLiveData<List<String>>().apply { value = emptyList() }
//    val deleteBookmarks: LiveData<List<String>>
//        get() = _deleteBookmarks
  private val deleteBookmarkList = ArrayList<String>()

  private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
  val categories: LiveData<List<Category>>
    get() = _categories

  private val _snackbarText = MutableLiveData<Event<Int>>()
  val snackbarMessage: LiveData<Event<Int>>
    get() = _snackbarText

  private val _currentCategory = MutableLiveData<String>()
  val currentCategory: LiveData<String>
    get() = _currentCategory

  private val _isCategoriesSetup = MutableLiveData<Boolean>()
  val isCategoriesSetup: LiveData<Boolean>
    get() = _isCategoriesSetup

  private val _isSelectedAll = MutableLiveData<Boolean>()
  val isSelectedAll: LiveData<Boolean>
    get() = _isSelectedAll

  private val isDataLoadingError = MutableLiveData<Boolean>()

  private val _dataLoading = MutableLiveData<Boolean>()
  val dataLoading: LiveData<Boolean>
    get() = _dataLoading

  internal fun clickedCategory(categoryId: String) {
    _currentCategory.value = categoryId
    _isCategoriesSetup.value = false
    // _deleteBookmarks.value = emptyList()
    deleteBookmarkList.clear()
    loadItems(false, showLoadingUI = false)
  }

  internal fun selectBookmark(bookmarkId: String, isSelected: Boolean) {
    if (isSelected) {
      deleteBookmarkList.add(bookmarkId)
    } else {
      deleteBookmarkList.remove(bookmarkId)
    }
    // Log.e("", "$deleteBookmarkList")
    // _deleteBookmarks.value = deleteBookmarkList
  }

  fun selectAllBookmark(isSelected: Boolean) {
    _isCategoriesSetup.value = false
    _isSelectedAll.value = isSelected
    // _deleteBookmarks.value = emptyList()
    deleteBookmarkList.clear()

    if (isSelected) {
      for (bookmark in bookmarks.value!!) {
        deleteBookmarkList.add(bookmark.id)
      }
    }
    // _deleteBookmarks.value = deleteBookmarkList
    loadItems(false, showLoadingUI = false)
    // Log.e("", "$deleteBookmarkList")
  }

  fun deleteBookmarks() {
    if (deleteBookmarkList.size == 0) {
      _snackbarText.value = Event(R.string.no_items_selected)
    } else {
      for (bookmarkId in deleteBookmarkList) {
        itemsRepository.deleteBookmark(bookmarkId)
      }
      deleteBookmarkList.clear()
      // _deleteBookmarks.value = emptyList()
      _snackbarText.value = Event(R.string.deleted_bookmarks)
      _isCategoriesSetup.value = false
      _isSelectedAll.value = false
      loadItems(false, showLoadingUI = false)
    }
  }

  fun start() {
    _isSelectedAll.value = false
    loadItems(false)
  }

  private fun loadItems(forceUpdate: Boolean) = loadItems(forceUpdate, true)

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
          for (bookmark in bookmarks) {
            if (bookmark.categoryId == category.id)
              isBookmarkEmpty = false
          }
          if (isBookmarkEmpty) {
            itemsRepository.deleteCategory(category.id)
          } else {
            categoriesToShow.add(category)
          }
        }

        for (bookmark in bookmarks) {
          if (bookmark.categoryId == currentCategory.value)
            bookmarksToShow.add(bookmark)
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