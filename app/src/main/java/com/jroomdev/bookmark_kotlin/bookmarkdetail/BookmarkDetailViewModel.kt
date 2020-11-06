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
package com.jroomdev.bookmark_kotlin.bookmarkdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jroomdev.bookmark_kotlin.Event
import com.jroomdev.bookmark_kotlin.data.Bookmark
import com.jroomdev.bookmark_kotlin.data.Category
import com.jroomdev.bookmark_kotlin.data.source.ItemsDataSource
import com.jroomdev.bookmark_kotlin.data.source.ItemsRepository
import java.text.SimpleDateFormat

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

    private val _selectedAt = MutableLiveData<String>()
    val selectedAt: LiveData<String>
        get() = _selectedAt

    val bookmarkId: String?
        get() = bookmark.value?.id

    val categoryId: String?
        get() = bookmark.value?.categoryId

    fun start(bookmarkId: String?) {
        if (bookmarkId != null) {
            itemRepository.getBookmark(bookmarkId, this)
        }
    }

    private fun setBookmark(bookmark: Bookmark?) {
        _bookmark.value = bookmark
        _isDataAvailable.value = bookmark != null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBookmarkLoaded(bookmark: Bookmark) {
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        _selectedAt.value = sd.format(bookmark.selectedAt!!)
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
                Log.e("DetailViewModel","category not available")
            }
        })
    }
}