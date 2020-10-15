package com.example.bookmarkse_kotlin.data

import androidx.annotation.NonNull
import androidx.room.*
import com.example.bookmarkse_kotlin.data.source.local.Converters
import java.time.LocalDate
import java.util.*

@Entity(tableName= "bookmarks")
data class Bookmark @JvmOverloads constructor (
    @NonNull @ColumnInfo(name= "title") var title: String = "",
    @NonNull @ColumnInfo(name= "url") var url: String = "",
    @NonNull @ColumnInfo(name= "category") var category: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {

    @TypeConverters(Converters::class)
    @ColumnInfo(name= "selectedAt") var selectedAt: LocalDate? = null

    @ColumnInfo(name = "position") var position: Int = 0

    @ColumnInfo(name = "favicon") var favicon: String = ""

    val isEmpty
        get() = title.isEmpty()
}
