package com.jroomdev.bookmark_kotlin

import android.content.Context

import com.jroomdev.bookmark_kotlin.util.AppExecutors
import com.jroomdev.bookmark_kotlin.data.source.ItemsRepository
import com.jroomdev.bookmark_kotlin.data.source.local.ItemsLocalDataSource
import com.jroomdev.bookmark_kotlin.data.source.local.LocalDatabase
import com.jroomdev.bookmark_kotlin.data.FakeItemsRemoteDataSource

object Injection {

    fun provideBookmarkRepository(context: Context): ItemsRepository {
        val database = LocalDatabase.getInstance(context)
        return ItemsRepository.getInstance(
            ItemsLocalDataSource.getInstance(
                AppExecutors(),
                database.bookmarkDao(),
                database.categoryDao()),
            FakeItemsRemoteDataSource)
    }
}