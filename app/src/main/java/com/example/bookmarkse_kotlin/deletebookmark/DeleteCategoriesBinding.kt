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