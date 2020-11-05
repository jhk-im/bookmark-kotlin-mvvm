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
package com.example.bookmarkse_kotlin.data.source

import android.util.Log
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category


class ItemsRepository(
    private val itemsLocalDataSource: ItemsDataSource,
    private val itemsRemoteDataSource: ItemsDataSource
) : ItemsDataSource {

    var cachedBookmarks: LinkedHashMap<String, Bookmark> = LinkedHashMap()
    var cachedCategories: LinkedHashMap<String, Category> = LinkedHashMap()
    var cacheIsDirty = false

    private fun refreshCache(bookmarks: List<Bookmark>, categories: List<Category>) {
        cachedBookmarks.clear()
        bookmarks.forEach {
            cacheBookmarkAndPerform(it) {}
        }
        cachedCategories.clear()
        categories.forEach {
            cacheCategoryAndPerform(it) {}
        }

        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(bookmarks: List<Bookmark>, categories: List<Category>) {
        itemsLocalDataSource.deleteAllItems()
        for (bookmark in bookmarks) {
            itemsLocalDataSource.saveBookmark(
                "",
                bookmark,
                object : ItemsDataSource.GetCategoryCallback {
                    override fun onCategoryLoaded(categoryId: String) {
                        val nothing = null
                    }

                    override fun onDataNotAvailable() {
                        val nothing = null
                    }
                })
        }
        for (category in categories) {
            itemsLocalDataSource.saveCategory(category)
        }
    }

    private fun getItemsFromRemoteDataSource(callback: ItemsDataSource.LoadItemsCallback) {
        itemsRemoteDataSource.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {
                refreshCache(bookmarks, categories)
                refreshLocalDataSource(bookmarks, categories)
                callback.onItemsLoaded(
                    ArrayList(cachedBookmarks.values),
                    ArrayList(cachedCategories.values)
                )
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getBookmarkId(id: String) = cachedBookmarks[id]

    private inline fun cacheBookmarkAndPerform(bookmark: Bookmark, perform: (Bookmark) -> Unit) {
        val cachedBookmark =
            Bookmark(
                bookmark.title, bookmark.url, bookmark.id
            ).apply {
                categoryId = bookmark.categoryId
                favicon = bookmark.favicon
                selectedAt = bookmark.selectedAt
            }

        cachedBookmarks[cachedBookmark.id] = cachedBookmark
        perform(cachedBookmark)
    }

    private inline fun cacheCategoryAndPerform(category: Category, perform: (Category) -> Unit) {
        val cachedCategory =
            Category(category.title, category.id)
        cachedCategories[cachedCategory.title] = cachedCategory
        perform(cachedCategory)
    }

    override fun getItems(callback: ItemsDataSource.LoadItemsCallback) {

        if (cacheIsDirty) {
            getItemsFromRemoteDataSource(callback)
        } else {

            itemsLocalDataSource.getItems(object : ItemsDataSource.LoadItemsCallback {
                override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {
                    refreshCache(bookmarks, categories)
                    callback.onItemsLoaded(bookmarks, categories)
                }

                override fun onDataNotAvailable() {
                    getItemsFromRemoteDataSource(callback)
                }

            })
        }
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: ItemsDataSource.GetBookmarkCallback
    ) {
        val bookmarkInCache = getBookmarkId(bookmarkId)

        if (bookmarkInCache != null) {
            callback.onBookmarkLoaded(bookmarkInCache)
            return
        }

        itemsLocalDataSource.getBookmark(
            bookmarkId,
            object : ItemsDataSource.GetBookmarkCallback {

                override fun onBookmarkLoaded(bookmark: Bookmark) {

                    cacheBookmarkAndPerform(bookmark) {
                        callback.onBookmarkLoaded(it)
                    }
                }

                override fun onDataNotAvailable() {
                    itemsRemoteDataSource.getBookmark(
                        bookmarkId,
                        object : ItemsDataSource.GetBookmarkCallback {

                            override fun onBookmarkLoaded(bookmark: Bookmark) {
                                cacheBookmarkAndPerform(bookmark) {
                                    callback.onBookmarkLoaded(it)
                                }
                            }

                            override fun onDataNotAvailable() {
                                callback.onDataNotAvailable()
                            }
                        })
                }
            })
    }

    override fun saveBookmark(
        categoryTitle: String,
        bookmark: Bookmark,
        callback: ItemsDataSource.GetCategoryCallback
    ) {

        cacheBookmarkAndPerform(bookmark) {
            itemsLocalDataSource.saveBookmark(
                categoryTitle,
                it,
                object : ItemsDataSource.GetCategoryCallback {
                    override fun onCategoryLoaded(categoryId: String) {
                        callback.onCategoryLoaded(categoryId)
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            // itemsRemoteDataSource.saveBookmark(categoryTitle, it)
        }
    }

    override fun saveCategory(category: Category) {
        cacheCategoryAndPerform(category) {
            itemsLocalDataSource.saveCategory(it)
            itemsRemoteDataSource.saveCategory(it)
        }
    }

    override fun deleteCategory(categoryId: String) {
        itemsLocalDataSource.deleteCategory(categoryId)
        // itemsRemoteDataSource.deleteCategory(categoryId)
        cachedCategories.remove(categoryId)
    }

    override fun deleteAllItems() {
        itemsLocalDataSource.deleteAllItems()
        itemsRemoteDataSource.deleteAllItems()
        cachedBookmarks.clear()
    }

    override fun deleteBookmark(bookmarkId: String) {
        itemsLocalDataSource.deleteBookmark(bookmarkId)
        itemsRemoteDataSource.deleteBookmark(bookmarkId)
        cachedBookmarks.remove(bookmarkId)
    }

    override fun refreshBookmark() {
        cacheIsDirty = true
    }

    override fun selectedBookmark(bookmarkId: String) {
        getBookmarkId(bookmarkId)?.let {
            selectedBookmark(it)
        }
    }

    override fun selectedBookmark(bookmark: Bookmark) {
        cacheBookmarkAndPerform(bookmark) {
            itemsLocalDataSource.selectedBookmark(it)
            itemsRemoteDataSource.selectedBookmark(it)
        }
    }

    companion object {

        private var INSTANCE: ItemsRepository? = null

        @JvmStatic
        fun getInstance(
            itemsLocalDataSource: ItemsDataSource,
            itemsRemoteDataSource: ItemsDataSource
        ) =
            INSTANCE ?: synchronized(ItemsRepository::class.java) {
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
