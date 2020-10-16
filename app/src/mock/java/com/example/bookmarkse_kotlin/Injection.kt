package com.example.bookmarkse_kotlin

import android.content.Context
import com.example.bookmarkse_kotlin.data.FakeBookmarkRemoteDataSource
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository
import com.example.bookmarkse_kotlin.data.source.local.LocalDatabase
import com.example.bookmarkse_kotlin.data.source.local.BookmarkLocalDataSource
import com.example.bookmarkse_kotlin.util.AppExecutors

object Injection {

    fun provideBookmarksRepository(context: Context): BookmarkRepository {
        val database = LocalDatabase.getInstance(context)
        return BookmarkRepository.getInstance(
            BookmarkLocalDataSource.getInstance(
                AppExecutors(),
                database.bookmarkDao()
            ),
            FakeBookmarkRemoteDataSource
        )
    }
}