package com.example.bookmarkse_kotlin.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName= "bookmarks")
data class Bookmark @JvmOverloads constructor (
    @NonNull @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @NonNull @ColumnInfo(name= "title") var title: String = "",
    @NonNull @ColumnInfo(name= "url") var url: String = "",
    @NonNull @ColumnInfo(name= "category") var category: String = ""
) {

    @ColumnInfo(name = "position") var position: Int = 0

    @ColumnInfo(name = "favicon") var favicon: String = ""

    val isEmpty
        get() = title.isEmpty()
}
