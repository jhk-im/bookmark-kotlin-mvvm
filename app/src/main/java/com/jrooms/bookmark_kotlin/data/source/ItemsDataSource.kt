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
package com.jrooms.bookmark_kotlin.data.source

import com.jrooms.bookmark_kotlin.data.Bookmark
import com.jrooms.bookmark_kotlin.data.Category

interface ItemsDataSource {

  interface LoadItemsCallback {
    fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>)
    fun onDataNotAvailable()
  }

  interface GetBookmarkCallback {
    fun onBookmarkLoaded(book: Bookmark)
    fun onDataNotAvailable()
  }

  interface GetCategoryCallback {
    fun onCategoryLoaded(categoryId: String)
    fun onDataNotAvailable()
  }

  fun getItems(callback: LoadItemsCallback)

  fun getBookmark(bookmarkId: String, callback: GetBookmarkCallback)

  fun saveBookmark(categoryTitle: String, bookmark: Bookmark, callback: GetCategoryCallback)

  fun saveCategory(category: Category)

  fun deleteCategory(categoryId: String)

  fun deleteAllItems()

  fun deleteBookmark(bookmarkId: String)

  fun selectedBookmark(bookmarkId: String)

  fun selectedBookmark(bookmark: Bookmark)
}
