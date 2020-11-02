package com.example.bookmarkse_kotlin.data.source

import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category

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

    fun deleteAllItems()

    fun deleteBookmark(bookmarkId: String)

    fun refreshBookmark()

    fun selectedBookmark(bookmarkId: String)

    fun selectedBookmark(bookmark: Bookmark)
}
