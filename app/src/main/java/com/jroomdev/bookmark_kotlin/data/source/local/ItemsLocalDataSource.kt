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
package com.jroomdev.bookmark_kotlin.data.source.local

import androidx.annotation.VisibleForTesting
import com.jroomdev.bookmark_kotlin.data.Bookmark
import com.jroomdev.bookmark_kotlin.data.Category
import com.jroomdev.bookmark_kotlin.data.source.ItemsDataSource
import com.jroomdev.bookmark_kotlin.util.AppExecutors
import java.util.*

class ItemsLocalDataSource private constructor(
    private val appExecutors: AppExecutors,
    private val bookmarkDao: BookmarkDao,
    private val categoryDao: CategoryDao
) : ItemsDataSource {

    override fun getItems(callback: ItemsDataSource.LoadItemsCallback) {
        appExecutors.diskIO.execute {
            val bookmarks = bookmarkDao.getBookmarks()
            val categories = categoryDao.getCategories()
            appExecutors.mainThread.execute {
                if (bookmarks.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onItemsLoaded(bookmarks, categories)
                }
            }
        }
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: ItemsDataSource.GetBookmarkCallback
    ) {
        appExecutors.diskIO.execute {
            val bookmark = bookmarkDao.getBookmarkById(bookmarkId)
            appExecutors.mainThread.execute {
                if (bookmark != null) {
                    callback.onBookmarkLoaded(bookmark)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveBookmark(
        categoryTitle: String,
        bookmark: Bookmark,
        callback: ItemsDataSource.GetCategoryCallback
    ) {

        appExecutors.diskIO.execute {
            categoryTitle.let {
                val getCategory = categoryDao.getCategoryByTitle(categoryTitle)
                bookmark.categoryId = getCategory!!.id
                appExecutors.mainThread.execute {
                    callback.onCategoryLoaded(getCategory.id)
                }
                val selected = Date()
                bookmark.selectedAt = selected
            }

            bookmarkDao.insertBookmark(bookmark)
        }
    }

    override fun saveCategory(category: Category) {
        appExecutors.diskIO.execute {
            category.let {
                val selected = Date()
                category.selectedAt = selected
                if (categoryDao.getCategoryByTitle(category.title) == null) {
                    categoryDao.insertCategory(category)
                }
            }
        }
    }

    override fun deleteCategory(categoryId: String) {
        appExecutors.diskIO.execute { categoryDao.deleteCategoryById(categoryId) }
    }

    override fun deleteAllItems() {
        appExecutors.diskIO.execute {
            bookmarkDao.deleteBookmarks()
            categoryDao.deleteCategories()
        }
    }

    override fun deleteBookmark(bookmarkId: String) {
        appExecutors.diskIO.execute { bookmarkDao.deleteBookmarkById(bookmarkId) }
    }

    override fun refreshBookmark() {
        //
    }

    override fun selectedBookmark(bookmarkId: String) {
        //
    }

    override fun selectedBookmark(bookmark: Bookmark) {
        val selected = Date()
        appExecutors.diskIO.execute { bookmarkDao.selectedBookmarkById(bookmark.id, selected) }
    }

    companion object {
        private var INSTANCE: ItemsLocalDataSource? = null

        @JvmStatic
        fun getInstance(
            appExecutors: AppExecutors,
            bookmarkDao: BookmarkDao,
            categoryDao: CategoryDao
        ): ItemsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(ItemsLocalDataSource::javaClass) {
                    INSTANCE = ItemsLocalDataSource(appExecutors, bookmarkDao, categoryDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
