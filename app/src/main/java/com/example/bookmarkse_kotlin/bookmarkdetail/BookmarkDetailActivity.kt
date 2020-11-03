package com.example.bookmarkse_kotlin.bookmarkdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.databinding.BookmarkDetailActBinding
import com.example.bookmarkse_kotlin.util.obtainViewModel

class BookmarkDetailActivity : AppCompatActivity() {

    private lateinit var viewBinding: BookmarkDetailActBinding
    private lateinit var viewModel: BookmarkDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.bookmark_detail_act)
        viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
        viewBinding.viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
        viewModel.start(intent.getStringExtra(EXTRA_BOOKMARK_ID))

        this.setFinishOnTouchOutside(true)


        setFaviconImage()
    }

    private fun setFaviconImage() {
        Glide.with(viewBinding.root)
            .load(viewModel.bookmark.value?.favicon)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(viewBinding.ivUrlImage)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        finish()
    }

    companion object {
        const val EXTRA_BOOKMARK_ID = "BOOKMARK_ID"
    }
}