package com.example.bookmarkse_kotlin.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.databinding.CategoryItemBinding

class CategoryAdapter(
    private var categories: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(view)
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

    class CategoryViewHolder(private val viewBinding: CategoryItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindViewHolder(category: Category) {
            with(viewBinding) {
                this.category = category
                categoryChip.text = category.title
                executePendingBindings()
            }
        }

    }
}