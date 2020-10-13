package com.example.bookmarkse_kotlin.data.source.local

import androidx.annotation.VisibleForTesting
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.example.bookmarkse_kotlin.util.AppExecutors

class BookmarksLocalDataSource private constructor(
    private val appExecutors: AppExecutors,
    private val bookmarkDao: BookmarksDao
) : BookmarkDataSource {

    override fun getBookmarks(callback: BookmarkDataSource.LoadBookmarksCallback) {
        appExecutors.diskIO.execute {
            val bookmarks = bookmarkDao.getBookmarks()
            appExecutors.mainThread.execute {
                if (bookmarks.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onBookmarksLoaded(bookmarks)
                }
            }
        }
    }

    override fun getBookmark(
        bookmarkId: String,
        callback: BookmarkDataSource.GetBookmarkCallback
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

    override fun saveBookmark(bookmark: Bookmark) {
        appExecutors.diskIO.execute { bookmarkDao.insertBookmark(bookmark) }
    }

    override fun deleteAllBookmarks() {
        appExecutors.diskIO.execute { bookmarkDao.deleteBookmarks() }
    }

    override fun deleteBookmark(bookmarkId: String) {
        appExecutors.diskIO.execute { bookmarkDao.deleteBookmarkById(bookmarkId) }
    }

    companion object {
        private var INSTANCE: BookmarksLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, bookmarkDao: BookmarksDao): BookmarksLocalDataSource {
            if (INSTANCE == null) {
                synchronized(BookmarksLocalDataSource::javaClass) {
                    INSTANCE = BookmarksLocalDataSource(appExecutors, bookmarkDao)
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
