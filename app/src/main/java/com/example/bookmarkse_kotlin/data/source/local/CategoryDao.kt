package com.example.bookmarkse_kotlin.data.source.local

import androidx.room.*
import com.example.bookmarkse_kotlin.data.Category

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
