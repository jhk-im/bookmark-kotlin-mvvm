package com.example.bookmarkse_kotlin

import android.content.Context

import com.example.bookmarkse_kotlin.util.AppExecutors
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import com.example.bookmarkse_kotlin.data.source.local.ItemsLocalDataSource
import com.example.bookmarkse_kotlin.data.source.local.LocalDatabase
import com.example.bookmarkse_kotlin.data.FakeItemsRemoteDataSource

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