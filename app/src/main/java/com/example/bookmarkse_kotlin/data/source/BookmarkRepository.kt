package com.example.bookmarkse_kotlin.data.source

import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.local.BookmarksLocalDataSource

class BookmarkRepository(
    val bookmarksLocalDataSource: BookmarksLocalDataSource
) : BookmarkDataSource {

    var cachedBookmarks: LinkedHashMap<String, Bookmark> = LinkedHashMap()


    private fun getBookmarksFromRemoteDataSource(callback: BookmarkDataSource.LoadBookmarksCallback) {
        bookmarksLocalDataSource.getBookmarks(object : BookmarkDataSource.LoadBookmarksCallback {
            override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                callback.onBookmarksLoaded(ArrayList(cachedBookmarks.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun getBookmarkId(id: String) = cachedBookmarks[id]

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {

        if (cachedBookmarks.isNotEmpty()) {
            callback.onBookmarksLoaded(ArrayList(cachedBookmarks.values))
            return
        }

        bookmarksLocalDataSource.getBookmarks(object :
            BookmarkDataSource.LoadBookmarksCallback {
            override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                callback.onBookmarksLoaded(ArrayList(cachedBookmarks.values))
            }

            override fun onDataNotAvailable() {
                onDataNotAvailable()
            }
        })
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

        bookmarksLocalDataSource.getBookmark(
            bookmarkId,
            object : BookmarkDataSource.GetBookmarkCallback {
                override fun onBookmarkLoaded(book: Bookmark) {
                    cacheAndPerform(book) {
                        callback.onBookmarkLoaded(it)
                    }
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
    }

    override fun saveBookmark(bookmark: Bookmark) {
        cacheAndPerform(bookmark) {
            bookmarksLocalDataSource.saveBookmark(it)
        }
    }

    override fun deleteAllBookmarks() {
        bookmarksLocalDataSource.deleteAllBookmarks()
        cachedBookmarks.clear()
    }

    override fun deleteBookmark(bookmarkId: String) {
        bookmarksLocalDataSource.deleteBookmark(bookmarkId)
        cachedBookmarks.remove(bookmarkId)
    }

    private inline fun cacheAndPerform(bookmark: Bookmark, perform: (Bookmark) -> Unit) {
        val cachedBookmark =
            Bookmark(
                bookmark.id,
                bookmark.category,
                bookmark.url,
                bookmark.title,
                bookmark.position
            ).apply {
                favicon = bookmark.favicon
            }

        cachedBookmarks[cachedBookmark.id] = cachedBookmark
        perform(cachedBookmark)
    }

    companion object {

        private var INSTANCE: BookmarkRepository? = null

        @JvmStatic
        fun getInstance(bookmarksLocalDataSource: BookmarksLocalDataSource) =
            INSTANCE ?: synchronized(BookmarkRepository::class.java) {
                INSTANCE ?: BookmarkRepository(bookmarksLocalDataSource).also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
