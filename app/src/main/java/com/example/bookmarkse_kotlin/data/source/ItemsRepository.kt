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
            itemsLocalDataSource.saveBookmark("", bookmark)
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
                callback.onItemsLoaded(ArrayList(cachedBookmarks.values),ArrayList(cachedCategories.values))
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
//        if (cachedBookmarks.isNotEmpty() && !cacheIsDirty) {
//            callback.onItemsLoaded(
//                ArrayList(cachedBookmarks.values),
//                ArrayList(cachedCategories.values)
//            )
//            return
//        }

        if (cacheIsDirty) {
            getItemsFromRemoteDataSource(callback)
        } else {

            itemsLocalDataSource.getItems(object: ItemsDataSource.LoadItemsCallback {
                override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {
                    refreshCache(bookmarks, categories)
                    callback.onItemsLoaded(bookmarks,categories)
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

    override fun saveBookmark(categoryTitle: String, bookmark: Bookmark) {
        cacheBookmarkAndPerform(bookmark) {
            itemsLocalDataSource.saveBookmark(categoryTitle, it)
            itemsRemoteDataSource.saveBookmark(categoryTitle, it)
        }
    }

    override fun saveCategory(category: Category) {
        cacheCategoryAndPerform(category) {
            itemsLocalDataSource.saveCategory(it)
            itemsRemoteDataSource.saveCategory(it)
        }
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
