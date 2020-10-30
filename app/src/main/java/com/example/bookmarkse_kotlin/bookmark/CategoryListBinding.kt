package com.example.bookmarkse_kotlin.bookmark

import android.util.Log
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Category
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object CategoryListBinding {

    @BindingAdapter(value = ["app:categories", "app:viewModel"])
    @JvmStatic
    fun setCategories(
        view: ChipGroup,
        @NonNull categories: List<Category>,
        @NonNull viewModel: BookmarkViewModel
    ) {
        if(viewModel.isCategoriesSetup.value != null) {
            if(!viewModel.isCategoriesSetup.value!!)
                return

            val context = view.context
            view.removeAllViews()
            for (category in categories) {
                val chip = Chip(context)
                chip.text = category.title
                chip.isCheckable = true
                view.addView(chip)
                chip.setOnClickListener{
                    viewModel.clickedCategory(category.id)
                }
                if(categories.indexOf(category) == 0){
                    chip.performClick()
                }
            }
        }
    }
}