/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jrooms.bookmark_kotlin

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jrooms.bookmark_kotlin.addeditbookmark.AddEditBookmarkViewModel
import com.jrooms.bookmark_kotlin.bookmark.BookmarkViewModel
import com.jrooms.bookmark_kotlin.bookmarkdetail.BookmarkDetailViewModel
import com.jrooms.bookmark_kotlin.data.source.ItemsRepository
import com.jrooms.bookmark_kotlin.deletebookmark.DeleteBookmarkViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val itemsRepository: ItemsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
           when {
               isAssignableFrom(BookmarkViewModel::class.java) ->
                   BookmarkViewModel(itemsRepository)
               isAssignableFrom(AddEditBookmarkViewModel::class.java) ->
                   AddEditBookmarkViewModel(itemsRepository)
               isAssignableFrom(BookmarkDetailViewModel::class.java) ->
                   BookmarkDetailViewModel(itemsRepository)
               isAssignableFrom(DeleteBookmarkViewModel::class.java) ->
                   DeleteBookmarkViewModel(itemsRepository)
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