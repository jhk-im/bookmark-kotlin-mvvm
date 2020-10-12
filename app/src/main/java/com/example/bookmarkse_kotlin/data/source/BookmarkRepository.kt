package com.example.bookmarkse_kotlin.data.source

import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.local.BookmarksLocalDataSource

class BookmarkRepository(
    val bookmarksLocalDataSource: BookmarksLocalDataSource
) : BookmarkDataSource {

    var cachedBookmarks: LinkedHashMap<String, Bookmark> = LinkedHashMap()

    var cacheIsDirty = false

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

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {
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
        TODO("Not yet implemented")
    }

    override fun saveBookmark(bookmark: Bookmark) {

    }

    override fun deleteAllBookmarks() {
        TODO("Not yet implemented")
    }

    override fun deleteBookmark(bookmarkId: String) {
        TODO("Not yet implemented")
    }


}