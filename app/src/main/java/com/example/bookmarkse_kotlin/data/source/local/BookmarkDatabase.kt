package com.example.bookmarkse_kotlin.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookmarkse_kotlin.data.Bookmark

@Database(entities = [Bookmark::class], version = 1)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    companion object {

        private var INSTANCE: BookmarkDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): BookmarkDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BookmarkDatabase::class.java,
                        "bookmarks.db"
                    ).build()
                }
                return  INSTANCE!!
            }
        }
    }
}
