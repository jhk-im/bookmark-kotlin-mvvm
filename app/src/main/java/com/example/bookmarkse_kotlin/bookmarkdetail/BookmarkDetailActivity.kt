package com.example.bookmarkse_kotlin.bookmarkdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.example.bookmarkse_kotlin.addeditbookmark.AddEditBookmarkFragment
import com.example.bookmarkse_kotlin.bookmark.BookmarkActivity
import com.example.bookmarkse_kotlin.databinding.BookmarkDetailActBinding
import com.example.bookmarkse_kotlin.util.ADD_EDIT_RESULT_OK
import com.example.bookmarkse_kotlin.util.obtainViewModel

class BookmarkDetailActivity : AppCompatActivity(), BookMarkDetailUserActionListener {

    private lateinit var viewBinding: BookmarkDetailActBinding
    private lateinit var viewModel: BookmarkDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.bookmark_detail_act)
        viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
        viewBinding.viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
        viewModel.start(intent.getStringExtra(EXTRA_BOOKMARK_ID))

        this.setFinishOnTouchOutside(false)

        setFaviconImage()

        setOnclickListener()

    }

    private fun setOnclickListener() {
        viewBinding.editButton.setOnClickListener {
            openEditItem(viewModel.bookmarkId!!)
        }
    }

    private fun setFaviconImage() {
        Glide.with(viewBinding.root)
            .load(viewModel.bookmark.value?.favicon)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(viewBinding.ivUrlImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddEditBookmarkActivity.REQUEST_CODE) {
            when (resultCode) {
                ADD_EDIT_RESULT_OK -> {
                    val intent = Intent(this, BookmarkActivity::class.java).apply {
                        putExtra(
                            AddEditBookmarkActivity.CATEGORY_ID,
                            data?.getStringExtra(AddEditBookmarkActivity.CATEGORY_ID)
                        )
                    }
                    setResult(ADD_EDIT_RESULT_OK, intent)

                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun openWeb() {
        TODO("Not yet implemented")
    }

    override fun openEditItem(bookmarkId: String) {
        val intent = Intent(this, AddEditBookmarkActivity::class.java).apply {
            putExtra(AddEditBookmarkFragment.ARGUMENT_EDIT_ID, bookmarkId)
        }
        startActivityForResult(intent, AddEditBookmarkActivity.REQUEST_CODE)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    override fun shareUrl() {
        TODO("Not yet implemented")
    }

    companion object {
        const val EXTRA_BOOKMARK_ID = "BOOKMARK_ID"
    }
}