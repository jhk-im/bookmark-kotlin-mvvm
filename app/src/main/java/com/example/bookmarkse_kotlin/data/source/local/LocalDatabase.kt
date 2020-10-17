package com.example.bookmarkse_kotlin.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category

@Database(entities = [Bookmark::class, Category::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun categoryDao(): CategoryDao

    companion object {

        private var INSTANCE: LocalDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): LocalDatabase {

            synchronized(lock) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "localDatabase.db"
                    ).build()
                }

                return  INSTANCE!!
            }
        }
    }
}
