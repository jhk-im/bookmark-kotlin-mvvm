package com.example.bookmarkse_kotlin.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "categories")
data class Category @JvmOverloads constructor(
    @NonNull @ColumnInfo(name = "title") var title: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {
    @ColumnInfo(name = "selectedAt") var selectedAt: Date? = null

    val isEmpty
        get() = title.isEmpty()
}