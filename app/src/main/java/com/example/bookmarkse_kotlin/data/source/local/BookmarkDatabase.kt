package com.example.bookmarkse_kotlin.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookmarkse_kotlin.data.Bookmark

@Database(entities = [Bookmark::class], version = 1)
@TypeConverters(Converters::class)
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
