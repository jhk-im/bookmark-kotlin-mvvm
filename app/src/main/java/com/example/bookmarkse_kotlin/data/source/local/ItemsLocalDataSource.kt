package com.example.bookmarkse_kotlin.data.source.local

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.util.AppExecutors
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
