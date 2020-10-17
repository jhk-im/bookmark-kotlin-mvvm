package com.example.bookmarkse_kotlin.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "categories")
data class Category @JvmOverloads constructor(
    @NonNull @ColumnInfo(name = "categoryTitle") var categoryTitle: String = "",
    @PrimaryKey @ColumnInfo(name = "categoryId") var categoryId: String = UUID.randomUUID().toString()
) {

    @ColumnInfo(name = "isSelected") var isSelected: Boolean = false

    val isEmpty
        get() = categoryTitle.isEmpty()
}