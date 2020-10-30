package com.example.bookmarkse_kotlin.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.databinding.CategoryItemBinding

class CategoryAdapter(
    private var categories: List<Category>,
    val viewModel: BookmarkViewModel
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindViewHolder(categories[position])
    }

    override fun getItemCount() = categories.size

    fun replaceData(categories: List<Category>) = setList(categories)

    private fun setList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    class CategoryViewHolder(
        private val viewBinding: CategoryItemBinding,
        private val viewModel: BookmarkViewModel
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        private val userActionsListener = object : ItemUserActionsListener {
            override fun onBookmarkClicked(bookmark: Bookmark) {
                TODO("Not yet implemented")

            }

            override fun onCategoryClicked(category: Category) {
                viewModel.clickedCategory(category.id)
            }
        }

        fun bindViewHolder(category: Category) {
            with(viewBinding) {
                categoryChip.isChecked = true
                this.category = category
                categoryChip.text = category.title
                listener = userActionsListener
                executePendingBindings()
            }
        }
    }
}