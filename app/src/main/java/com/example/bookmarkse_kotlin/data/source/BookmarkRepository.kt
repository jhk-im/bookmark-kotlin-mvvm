package com.example.bookmarkse_kotlin.data.source

import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import java.time.LocalDate

class BookmarkRepository(
    private val bookmarkLocalDataSource: BookmarkDataSource,
    private val bookmarkRemoteDataSource: BookmarkDataSource
) : BookmarkDataSource {

    var cachedBookmarks: LinkedHashMap<String, Bookmark> = LinkedHashMap()

    var cacheIsDirty = false

    private fun refreshCache(bookmarks: List<Bookmark>) {
        cachedBookmarks.clear()
        bookmarks.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(bookmarks: List<Bookmark>) {
        bookmarkLocalDataSource.deleteAllBookmarks()
        for (bookmark in bookmarks) {
            bookmarkLocalDataSource.saveBookmark(bookmark)
        }
    }

    private fun getBookmarkFromRemoteDataSource(callback: BookmarkDataSource.LoadBookmarksCallback) {
        bookmarkRemoteDataSource.getBookmarks(object : BookmarkDataSource.LoadBookmarksCallback {
            override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                refreshCache(bookmarks)
                refreshLocalDataSource(bookmarks)

                callback.onBookmarksLoaded(ArrayList(cachedBookmarks.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    private fun getBookmarkId(id: String) = cachedBookmarks[id]

    private inline fun cacheAndPerform(bookmark: Bookmark, perform: (Bookmark) -> Unit) {
        val cachedBookmark =
            Bookmark(
                bookmark.title, bookmark.url, bookmark.selectedAt, bookmark.id
            ).apply {
                favicon = bookmark.favicon
                position = bookmark.position
            }

        cachedBookmarks[cachedBookmark.id] = cachedBookmark
        perform(cachedBookmark)
    }

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {

        if (cachedBookmarks.isNotEmpty() && !cacheIsDirty) {

            callback.onBookmarksLoaded(ArrayList(cachedBookmarks.values))
            return
        }

        if (cacheIsDirty) {
            getBookmarkFromRemoteDataSource(callback)
        } else {

            bookmarkLocalDataSource.getBookmarks(object :
                BookmarkDataSource.LoadBookmarksCallback {

                override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                    callback.onBookmarksLoaded(ArrayList(cachedBookmarks.values))
                }

                override fun onDataNotAvailable() {
                    getBookmarkFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: BookmarkDataSource.GetBookmarkCallback
    ) {

        val bookmarkInCache = getBookmarkId(bookmarkId)

        if (bookmarkInCache != null) {
            callback.onBookmarkLoaded(bookmarkInCache)
            return
        }

        bookmarkLocalDataSource.getBookmark(
            bookmarkId,
            object : BookmarkDataSource.GetBookmarkCallback {

                override fun onBookmarkLoaded(bookmark: Bookmark) {

                    cacheAndPerform(bookmark) {
                        callback.onBookmarkLoaded(it)
                    }
                }

                override fun onDataNotAvailable() {
                    bookmarkRemoteDataSource.getBookmark(
                        bookmarkId,
                        object : BookmarkDataSource.GetBookmarkCallback {

                            override fun onBookmarkLoaded(bookmark: Bookmark) {
                                cacheAndPerform(bookmark) {
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

    override fun saveBookmark(bookmark: Bookmark) {
        cacheAndPerform(bookmark) {
            bookmarkLocalDataSource.saveBookmark(it)
            bookmarkRemoteDataSource.saveBookmark(it)
        }
    }

    override fun deleteAllBookmarks() {
        bookmarkLocalDataSource.deleteAllBookmarks()
        bookmarkRemoteDataSource.deleteAllBookmarks()
        cachedBookmarks.clear()
    }

    override fun deleteBookmark(bookmarkId: String) {
        bookmarkLocalDataSource.deleteBookmark(bookmarkId)
        bookmarkRemoteDataSource.deleteBookmark(bookmarkId)
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
        cacheAndPerform(bookmark) {
            bookmarkLocalDataSource.selectedBookmark(it)
            bookmarkRemoteDataSource.selectedBookmark(it)
        }
    }

    companion object {

        private var INSTANCE: BookmarkRepository? = null

        @JvmStatic
        fun getInstance(
            bookmarkLocalDataSource: BookmarkDataSource,
            bookmarkRemoteDataSource: BookmarkDataSource
        ) =
            INSTANCE ?: synchronized(BookmarkRepository::class.java) {
                INSTANCE ?: BookmarkRepository(
                    bookmarkLocalDataSource,
                    bookmarkRemoteDataSource
                ).also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
