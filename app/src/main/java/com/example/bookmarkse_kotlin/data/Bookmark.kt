package com.example.bookmarkse_kotlin.data

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "bookmarks")
data class Bookmark @JvmOverloads constructor(
    @NonNull @ColumnInfo(name = "title") var title: String = "",
    @NonNull @ColumnInfo(name = "url") var url: String = "",
    @ColumnInfo(name = "selectedAt") var selectedAt: Date?,
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {

    @ColumnInfo(name = "categoryId")
    var categoryId: String = ""

    @ColumnInfo(name = "position")
    var position: Int = 0

    @ColumnInfo(name = "favicon")
    var favicon: String = ""

    val isEmpty
        get() = title.isEmpty()
}
