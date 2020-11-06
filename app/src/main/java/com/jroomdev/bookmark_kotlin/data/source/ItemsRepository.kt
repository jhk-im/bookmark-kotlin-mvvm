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
package com.jroomdev.bookmark_kotlin.data.source

import com.jroomdev.bookmark_kotlin.data.Bookmark
import com.jroomdev.bookmark_kotlin.data.Category


class ItemsRepository(
    private val itemsLocalDataSource: ItemsDataSource,
    private val itemsRemoteDataSource: ItemsDataSource
) : ItemsDataSource {

    override fun getItems(callback: ItemsDataSource.LoadItemsCallback) {

        itemsLocalDataSource.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {
                callback.onItemsLoaded(bookmarks, categories)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })

    }

    override fun getBookmark(bookmarkId: String, callback: ItemsDataSource.GetBookmarkCallback) {
        itemsLocalDataSource.getBookmark(
            bookmarkId,
            object : ItemsDataSource.GetBookmarkCallback {
                override fun onBookmarkLoaded(bookmark: Bookmark) {
                    callback.onBookmarkLoaded(bookmark)
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
    }

    override fun saveBookmark(
        categoryTitle: String,
        bookmark: Bookmark,
        callback: ItemsDataSource.GetCategoryCallback
    ) {
        itemsLocalDataSource.saveBookmark(
            categoryTitle,
            bookmark,
            object : ItemsDataSource.GetCategoryCallback {
                override fun onCategoryLoaded(categoryId: String) {
                    callback.onCategoryLoaded(categoryId)
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
    }

    override fun saveCategory(category: Category) {
        itemsLocalDataSource.saveCategory(category)
        // itemsRemoteDataSource.saveCategory(category)
    }

    override fun deleteCategory(categoryId: String) {
        itemsLocalDataSource.deleteCategory(categoryId)
        // itemsRemoteDataSource.deleteCategory(categoryId)
    }

    override fun deleteAllItems() {
        itemsLocalDataSource.deleteAllItems()
        // itemsRemoteDataSource.deleteAllItems()
    }

    override fun deleteBookmark(bookmarkId: String) {
        itemsLocalDataSource.deleteBookmark(bookmarkId)
        // itemsRemoteDataSource.deleteBookmark(bookmarkId)
    }

    override fun refreshBookmark() {
        // cacheIsDirty = true
    }

    override fun selectedBookmark(bookmarkId: String) {
        itemsLocalDataSource.selectedBookmark(bookmarkId)
        //itemsRemoteDataSource.selectedBookmark(bookmarkId)
    }

    override fun selectedBookmark(bookmark: Bookmark) {
        itemsLocalDataSource.selectedBookmark(bookmark)
        //itemsRemoteDataSource.selectedBookmark(bookmark)
    }

    companion object {

        private var INSTANCE: ItemsRepository? = null

        @JvmStatic
        fun getInstance(
            itemsLocalDataSource: ItemsDataSource,
            itemsRemoteDataSource: ItemsDataSource
        ) = INSTANCE ?: synchronized(ItemsRepository::class.java) {
            INSTANCE ?: ItemsRepository(
                itemsLocalDataSource,
                itemsRemoteDataSource
            ).also { INSTANCE = it }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
