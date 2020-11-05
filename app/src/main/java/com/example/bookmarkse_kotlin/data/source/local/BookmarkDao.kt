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
package com.example.bookmarkse_kotlin.data.source.local

import androidx.room.*
import com.example.bookmarkse_kotlin.data.Bookmark
import java.util.*

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY selectedAt DESC")
    fun getBookmarks(): List<Bookmark>

    @Query("SELECT * FROM bookmarks WHERE id = :bookmarkId")
    fun getBookmarkById(bookmarkId: String): Bookmark?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark)

    @Update
    fun updateBookmark(bookmark: Bookmark): Int

    @Query("DELETE FROM bookmarks WHERE id = :bookmarkId")
    fun deleteBookmarkById(bookmarkId: String): Int

    @Query("DELETE FROM bookmarks") fun deleteBookmarks()

    @Query("UPDATE bookmarks SET selectedAt = :selectedAt WHERE id = :bookmarkId ")
    fun selectedBookmarkById(bookmarkId: String, selectedAt: Date): Int
}
