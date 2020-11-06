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
package com.jroomdev.bookmark_kotlin.data.source.local

import androidx.room.*
import com.jroomdev.bookmark_kotlin.data.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY selectedAt DESC")
    fun getCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE title = :title")
    fun getCategoryByTitle(title: String): Category?

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: String): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category): Int

    @Query("DELETE FROM categories WHERE id = :id")
    fun deleteCategoryById(id: String): Int

    @Query("DELETE FROM categories") fun deleteCategories()
}
