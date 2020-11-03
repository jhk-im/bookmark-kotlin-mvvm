package com.example.bookmarkse_kotlin.addeditbookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import com.example.bookmarkse_kotlin.data.Category
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

object EditCategoriesBinding {

    @BindingAdapter(value = ["app:editCategories", "app:editViewModel"])
    @JvmStatic
    fun setEditCategories(
        view: ChipGroup,
        @NonNull categories: List<Category>,
        @NonNull viewModel: AddEditBookmarkViewModel
    ) {

        val context = view.context
        view.removeAllViews()
        for (category in categories) {
            val chip = Chip(context)
            chip.text = category.title
            chip.isCheckable = true
            view.addView(chip)
            if (viewModel.categoryTitle.value == category.title) {
                chip.performClick()
            }
            chip.setOnClickListener {
                viewModel.categoryCheck(category.title)
            }
        }
    }
}
