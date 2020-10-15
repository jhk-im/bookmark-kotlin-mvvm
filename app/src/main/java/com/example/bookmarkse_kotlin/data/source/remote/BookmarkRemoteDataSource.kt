package com.example.bookmarkse_kotlin.data.source.remote

import android.os.Handler
import android.os.Looper
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.google.common.collect.Lists


object BookmarkRemoteDataSource : BookmarkDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    private var BOOKMARKS_SERVICE_DATA = LinkedHashMap<String, Bookmark>(2)

    init {
        addBookmark("Google", "Portal","https://www.google.com")
        addBookmark("Naver", "Portal","https://www.naver.com")
        addBookmark("Daum", "Portal","https://www.daum.net")
    }

    private fun addBookmark(title: String, category: String, url: String) {

        val newBookmark = Bookmark(title, category, url)

        BOOKMARKS_SERVICE_DATA.put(newBookmark.id, newBookmark)
    }

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {

        val bookmarks = Lists.newArrayList<Bookmark>(BOOKMARKS_SERVICE_DATA.values)

        // Simulate network
        Handler(Looper.getMainLooper()).postDelayed({
            callback.onBookmarksLoaded(bookmarks)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: BookmarkDataSource.GetBookmarkCallback
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

    override fun saveBookmark(bookmark: Bookmark) {

        BOOKMARKS_SERVICE_DATA.put(bookmark.id, bookmark)
    }

    override fun deleteAllBookmarks() {

        BOOKMARKS_SERVICE_DATA.clear()
    }

    override fun deleteBookmark(bookmarkId: String) {

        BOOKMARKS_SERVICE_DATA.remove(bookmarkId)
    }

    override fun refreshBookmark() {
        //
    }
}
