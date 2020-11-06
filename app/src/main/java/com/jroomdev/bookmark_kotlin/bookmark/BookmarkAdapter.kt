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
package com.jroomdev.bookmark_kotlin.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jroomdev.bookmark_kotlin.data.Bookmark
import com.jroomdev.bookmark_kotlin.databinding.BookmarkItemBinding

class BookmarkAdapter(
    private var bookmarks: List<Bookmark>,
    private var viewModel: BookmarkViewModel
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = BookmarkItemBinding.inflate(inflater,parent,false)
        return BookmarkViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bindViewHolder(bookmarks[position], position)
    }

    override fun getItemCount(): Int = bookmarks.size

    private fun setBookmarks(bookmarks: List<Bookmark>) {
        this.bookmarks = bookmarks
        notifyDataSetChanged()
    }

    fun replaceBookmarks(bookmarks: List<Bookmark>) = setBookmarks(bookmarks)

    class BookmarkViewHolder(private val viewBinding: BookmarkItemBinding, private val viewModel: BookmarkViewModel) :
        RecyclerView.ViewHolder(viewBinding.root) {

        fun bindViewHolder(bookmark: Bookmark, position: Int) {
            with(viewBinding){
                this.bookmark = bookmark
                Glide.with(viewBinding.root)
                    .load(bookmark.favicon)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(ivUrlImage)
                executePendingBindings()
            }

            viewBinding.clBookmark.setOnClickListener {
                viewModel.openBookmark(
                    bookmark.id,
                    viewBinding.ivUrlImage,
                    viewBinding.tvBookmarkTitle,
                    viewBinding.tvBookmarkUrl
                )
            }
        }
    }
}