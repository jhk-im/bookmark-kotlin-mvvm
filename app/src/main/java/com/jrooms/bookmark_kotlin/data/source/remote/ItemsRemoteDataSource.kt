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
package com.jrooms.bookmark_kotlin.data.source.remote

import android.os.Handler
import android.os.Looper
import com.jrooms.bookmark_kotlin.data.Bookmark
import com.jrooms.bookmark_kotlin.data.Category
import com.jrooms.bookmark_kotlin.data.source.ItemsDataSource
import java.util.Date

object ItemsRemoteDataSource : ItemsDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var BOOKMARKS_SERVICE_DATA = LinkedHashMap<String, Bookmark>(2)
    private var CATEGORIES_SERVICE_DATA = LinkedHashMap<String, Category>(2)

//    init {
//        addItems("Google", "Portal", "https://www.google.com")
//        addItems("Naver", "Portal", "https://www.naver.com")
//        addItems("Daum", "Portal", "https://www.daum.net")
//        addItems("COUPANG", "Shop", "https://www.coupang.com")
//    }

    private fun addItems(title: String, category: String, url: String) {
        val selected = Date()
        val newCategory = Category(category).apply { selectedAt = selected }
        if (CATEGORIES_SERVICE_DATA[newCategory.title] == null) {
            val newBookmark =
                Bookmark(title, url).apply {
                    selectedAt = selected
                    categoryId = newCategory.id
                }

            CATEGORIES_SERVICE_DATA[newCategory.title] = newCategory
            BOOKMARKS_SERVICE_DATA[newBookmark.id] = newBookmark
        } else {
            val newBookmark =
                Bookmark(title, url).apply {
                    selectedAt = selected
                    categoryId = CATEGORIES_SERVICE_DATA[newCategory.title]?.id.toString()
                }
            BOOKMARKS_SERVICE_DATA[newBookmark.id] = newBookmark
        }
    }

    override fun getItems(callback: ItemsDataSource.LoadItemsCallback) {

        val bookmarks = ArrayList<Bookmark>(BOOKMARKS_SERVICE_DATA.values)
        val categories = ArrayList<Category>(CATEGORIES_SERVICE_DATA.values)

        // Simulate network
        Handler(Looper.getMainLooper()).postDelayed({
            callback.onItemsLoaded(bookmarks, categories)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: ItemsDataSource.GetBookmarkCallback
    ) {

        val bookmark = BOOKMARKS_SERVICE_DATA[bookmarkId]

        // simulate network
        with(Handler(Looper.getMainLooper())) {
            if (bookmark != null) {
                postDelayed({ callback.onBookmarkLoaded(bookmark) }, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable() }, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun saveBookmark(
        categoryTitle: String,
        bookmark: Bookmark,
        callback: ItemsDataSource.GetCategoryCallback
    ) {
        val categoryId = CATEGORIES_SERVICE_DATA[categoryTitle]?.id
        if (categoryId != null) {
            bookmark.categoryId = categoryId
            callback.onCategoryLoaded(categoryId)
        } else {
            callback.onDataNotAvailable()
        }
        BOOKMARKS_SERVICE_DATA[bookmark.id] = bookmark
    }

    override fun saveCategory(category: Category) {
        val getCategory = CATEGORIES_SERVICE_DATA[category.title]
        if (getCategory != null) {
            val selected = Date()
            val newCategory = Category(category.title).apply { selectedAt = selected }
            CATEGORIES_SERVICE_DATA[newCategory.title] = newCategory
        }
    }

    override fun deleteCategory(categoryId: String) {
        CATEGORIES_SERVICE_DATA.remove(categoryId)
    }

    override fun deleteAllItems() {
        CATEGORIES_SERVICE_DATA.clear()
        BOOKMARKS_SERVICE_DATA.clear()
    }

    override fun deleteBookmark(bookmarkId: String) {

        BOOKMARKS_SERVICE_DATA.remove(bookmarkId)
    }

    override fun refreshBookmark() {
        //
    }

    override fun selectedBookmark(bookmarkId: String) {
        //
    }

    override fun selectedBookmark(bookmark: Bookmark) {
        val selected = Date()
        val selectedBookmark =
            Bookmark(bookmark.title, bookmark.url).apply { selectedAt = selected }

        BOOKMARKS_SERVICE_DATA[bookmark.id] = selectedBookmark
    }
}
