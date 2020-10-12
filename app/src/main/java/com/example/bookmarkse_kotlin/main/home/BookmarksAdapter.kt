package com.example.bookmarkse_kotlin.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.databinding.BookmarkItemBinding
import java.lang.IllegalStateException

class BookmarksAdapter(
    private var bookmarks: List<Bookmark>
) : BaseAdapter() {

    fun replaceData(bookmarks: List<Bookmark>) {
        setList(bookmarks)
    }

    private fun setList(bookmarks: List<Bookmark>) {
        this.bookmarks = bookmarks
        notifyDataSetChanged()
    }

    override fun getCount() = bookmarks.size

    override fun getItem(position: Int) = bookmarks[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: BookmarkItemBinding
        binding = if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)

            BookmarkItemBinding.inflate(inflater, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView) ?: throw IllegalStateException()
        }

        val userActionListener = object : BookmarkUserActionListener {
            override fun onCompleteChanged(bookmark: Bookmark, v: View) {

            }

            override fun onBookmarkClicked(bookmark: Bookmark) {
                TODO("Not yet implemented")
            }
        }

        with(binding) {
            bookmark = bookmarks[position]
            listener = userActionListener
            executePendingBindings()
        }

        return binding.root
    }



}