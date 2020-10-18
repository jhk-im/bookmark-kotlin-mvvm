package com.example.bookmarkse_kotlin.data.source.local

import androidx.room.*
import com.example.bookmarkse_kotlin.data.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE categoryTitle = :categoryTitle")
    fun getCategoryByTitle(categoryTitle: String): Category?

    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    fun getCategoryById(categoryId: String): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category): Int

    @Query("DELETE FROM categories WHERE categoryId = :categoryId")
    fun deleteCategoryById(categoryId: String): Int

    @Query("DELETE FROM categories") fun deleteCategories()

    @Query("UPDATE categories SET isSelected = :isSelected WHERE categoryId = :categoryId ")
    fun selectedCategoryById(categoryId: String, isSelected: Boolean): Int
}
