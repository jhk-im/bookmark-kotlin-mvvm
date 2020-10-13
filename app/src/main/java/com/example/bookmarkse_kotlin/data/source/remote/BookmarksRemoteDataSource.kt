package com.example.bookmarkse_kotlin.data.source.remote

import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource

object BookmarksRemoteDataSource : BookmarkDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var TASKS_SERVICE_DATA = LinkedHashMap<String, Bookmark>(2)

    init {

    }

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {
        TODO("Not yet implemented")
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: BookmarkDataSource.GetBookmarkCallback
    ) {
        TODO("Not yet implemented")
    }

    override fun saveBookmark(bookmark: Bookmark) {
        TODO("Not yet implemented")
    }

    override fun deleteAllBookmarks() {
        TODO("Not yet implemented")
    }

    override fun deleteBookmark(bookmarkId: String) {
        TODO("Not yet implemented")
    }
}
