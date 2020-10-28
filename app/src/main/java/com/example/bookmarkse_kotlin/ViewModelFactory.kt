package com.example.bookmarkse_kotlin

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel
import com.example.bookmarkse_kotlin.data.Injection
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val bookmarkRepository: ItemsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
           when {
               isAssignableFrom(BookmarkViewModel::class.java) ->
                   BookmarkViewModel(bookmarkRepository)
               else ->
                   throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
           }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideBookmarkRepository(application.applicationContext)
                ).also { INSTANCE = it }
            }

        @VisibleForTesting fun destroyInstance() {
            INSTANCE = null
        }
    }
}