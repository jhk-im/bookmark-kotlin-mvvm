package com.example.bookmarkse_kotlin

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.databinding.adapters.VideoViewBindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository

class ViewModelFactory private constructor(
    private val bookmarkRepository: BookmarkRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
           when {
               //
           }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                //INSTANCE ?: ViewModelFactory()
            }

        @VisibleForTesting fun destroyInstance() {
            INSTANCE = null
        }
    }
}