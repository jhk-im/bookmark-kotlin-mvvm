package com.example.bookmarkse_kotlin.bookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.data.Category

object CategoryListBinding {

    @BindingAdapter("app:categories")
    @JvmStatic
    fun setCategories(list: RecyclerView, @NonNull categories: List<Category>) {
        with(list.adapter as? CategoryAdapter) {
            this?.replaceData(categories)
        }
    }
}