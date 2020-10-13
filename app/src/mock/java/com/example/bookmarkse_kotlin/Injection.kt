package com.example.bookmarkse_kotlin

import android.content.Context
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository
import com.example.bookmarkse_kotlin.data.source.local.BookmarkDatabase
import com.example.bookmarkse_kotlin.data.source.local.BookmarksLocalDataSource
import com.example.bookmarkse_kotlin.util.AppExecutors

object Injection {

    fun provideBookmarksRepository(context: Context): BookmarkRepository {
        val database = BookmarkDatabase.getInstance(context)
        return BookmarkRepository.getInstance(
            BookmarksLocalDataSource.getInstance(
                AppExecutors(),
                database.bookmarkDao()
            )
        )
    }
}