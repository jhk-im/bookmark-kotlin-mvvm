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
package com.jroomdev.bookmark_kotlin.data

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "bookmarks")
data class Bookmark @JvmOverloads constructor(
    @NonNull @ColumnInfo(name = "title") var title: String = "",
    @NonNull @ColumnInfo(name = "url") var url: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
) {
    @ColumnInfo(name = "selectedAt")
    var selectedAt: Date? = null

    @ColumnInfo(name = "categoryId")
    var categoryId: String = ""

    @ColumnInfo(name = "favicon")
    var favicon: String = ""

    val isEmpty
        get() = title.isEmpty()
}
