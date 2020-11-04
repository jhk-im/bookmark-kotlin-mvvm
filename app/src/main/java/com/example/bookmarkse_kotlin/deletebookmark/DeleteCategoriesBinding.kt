package com.example.bookmarkse_kotlin.deletebookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel
import com.example.bookmarkse_kotlin.data.Category
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object DeleteCategoriesBinding {

    @BindingAdapter(value = ["app:deleteCategories", "app:deleteViewModel"])
    @JvmStatic
    fun setDeleteCategories(
        view: ChipGroup,
        @NonNull categories: List<Category>,
        @NonNull viewModel: DeleteBookmarkViewModel
    ){
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
                if (viewModel.currentCategory.value == category.id) {
                    chip.performClick()
                }
                chip.setOnClickListener{
                    viewModel.clickedCategory(category.id)
                }
            }
        }
    }
}