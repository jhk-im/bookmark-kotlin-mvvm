package com.example.bookmarkse_kotlin.data.source.local

import androidx.room.*
import com.example.bookmarkse_kotlin.data.Bookmark

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks")
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
}