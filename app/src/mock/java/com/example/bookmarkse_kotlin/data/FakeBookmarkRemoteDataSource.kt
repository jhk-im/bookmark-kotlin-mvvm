package com.example.bookmarkse_kotlin.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.google.common.collect.Lists
import java.time.LocalDate
import java.util.*
import kotlin.collections.LinkedHashMap

object FakeBookmarkRemoteDataSource : BookmarkDataSource {

    private var BOOKMARK_SERVICE_DATA: LinkedHashMap<String, Bookmark> = LinkedHashMap()

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {
        callback.onBookmarksLoaded(Lists.newArrayList(BOOKMARK_SERVICE_DATA.values))
    }

    override fun getBookmark(bookmarkId: String, callback: BookmarkDataSource.GetBookmarkCallback) {
        val bookmark = BOOKMARK_SERVICE_DATA[bookmarkId]
        bookmark?.let { callback.onBookmarkLoaded(it) }
    }

    override fun saveBookmark(bookmark: Bookmark) {
        BOOKMARK_SERVICE_DATA.put(bookmark.id, bookmark)
    }

    override fun deleteAllBookmarks() {
        BOOKMARK_SERVICE_DATA.clear()
    }

    override fun deleteBookmark(bookmarkId: String) {
        BOOKMARK_SERVICE_DATA.remove(bookmarkId)
    }

    override fun refreshBookmark() {
        //
    }

    override fun selectedBookmark(bookmarkId: String) {
        //
    }

    override fun selectedBookmark(bookmark: Bookmark) {
        val localDate = Date()
        val selectedBookmark =
            Bookmark(bookmark.title, bookmark.url, bookmark.category,localDate, bookmark.id)
        BOOKMARK_SERVICE_DATA.put(bookmark.id, selectedBookmark)
    }
}