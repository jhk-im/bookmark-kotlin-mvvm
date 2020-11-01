package com.example.bookmarkse_kotlin.bookmarkdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.databinding.BookmarkDetailActBinding
import com.example.bookmarkse_kotlin.util.DETAIL_RESULT_OK
import com.example.bookmarkse_kotlin.util.obtainViewModel

class BookmarkDetailActivity : AppCompatActivity() {

    private lateinit var viewBinding: BookmarkDetailActBinding
    private lateinit var viewModel: BookmarkDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.bookmark_detail_act)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.bookmark_detail_act)
        viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
        viewBinding.viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
        viewModel.start(intent.getStringExtra(EXTRA_BOOKMARK_ID))

        Log.e("title","${viewModel.bookmark.value?.title}")
        Log.e("url","${viewModel.bookmark.value?.url}")
        setBookmarkData()

    }

    private fun setBookmarkData() {
        Glide.with(viewBinding.root)
            .load(viewModel.bookmark.value?.favicon)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(viewBinding.ivUrlImage)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(DETAIL_RESULT_OK)
    }

    companion object {
        const val EXTRA_BOOKMARK_ID = "BOOKMARK_ID"
    }
}