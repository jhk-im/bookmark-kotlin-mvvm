package com.example.bookmarkse_kotlin.data

import androidx.annotation.VisibleForTesting
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

object FakeItemsRemoteDataSource : ItemsDataSource {

    private var BOOKMARKS_SERVICE_DATA: LinkedHashMap<String, Bookmark> = LinkedHashMap()
    private var CATEGORIES_SERVICE_DATA = LinkedHashMap<String, Category>()

    override fun getItems(callback: ItemsDataSource.LoadItemsCallback) {
        val bookmarks = ArrayList<Bookmark>()
        val categories = ArrayList<Category>()
        callback.onItemsLoaded(bookmarks,categories)
    }

    override fun getBookmark(bookmarkId: String, callback: ItemsDataSource.GetBookmarkCallback) {
        val bookmark = BOOKMARKS_SERVICE_DATA[bookmarkId]
        bookmark?.let { callback.onBookmarkLoaded(it) }
    }

    override fun saveBookmark(
        categoryTitle: String,
        bookmark: Bookmark,
        callback: ItemsDataSource.GetCategoryCallback
    ) {
        val categories = ArrayList<Category>()
        var categoryId = ""
        var isNewCategory = true
        for (category in categories) {
            if (category.id == bookmark.categoryId){
                isNewCategory = false
                categoryId = category.id
            }
        }
        if (isNewCategory){
            val newCategory = Category(categoryTitle)
            categoryId = newCategory.id
            CATEGORIES_SERVICE_DATA[newCategory.id] = newCategory
        }

        BOOKMARKS_SERVICE_DATA[bookmark.id] = bookmark
        callback.onCategoryLoaded(categoryId)
    }

    override fun saveCategory(category: Category) {
        //
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

    @VisibleForTesting
    fun addItems(vararg bookmarks: Bookmark) {
        for (bookmark in bookmarks) {
            BOOKMARKS_SERVICE_DATA[bookmark.id] = bookmark
        }
    }
}