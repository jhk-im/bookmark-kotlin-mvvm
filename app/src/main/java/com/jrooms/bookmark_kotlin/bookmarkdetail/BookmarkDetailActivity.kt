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
package com.jrooms.bookmark_kotlin.bookmarkdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jrooms.bookmark_kotlin.R
import com.jrooms.bookmark_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.jrooms.bookmark_kotlin.addeditbookmark.AddEditBookmarkFragment
import com.jrooms.bookmark_kotlin.bookmark.BookmarkActivity
import com.jrooms.bookmark_kotlin.databinding.BookmarkDetailActBinding
import com.jrooms.bookmark_kotlin.util.ADD_EDIT_RESULT_OK
import com.jrooms.bookmark_kotlin.util.obtainViewModel
import com.jrooms.bookmark_kotlin.webview.WebViewActivity

class BookmarkDetailActivity : AppCompatActivity() {

  private lateinit var viewBinding: BookmarkDetailActBinding
  private lateinit var viewModel: BookmarkDetailViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewBinding = DataBindingUtil.setContentView(this, R.layout.bookmark_detail_act)
    viewBinding.lifecycleOwner = this
    viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
    viewBinding.viewModel = obtainViewModel(BookmarkDetailViewModel::class.java, this)
    viewModel.start(intent.getStringExtra(EXTRA_BOOKMARK_ID))
    this.setFinishOnTouchOutside(false)

    setFaviconImage()

    setOnclickListener()

  }

  private fun setOnclickListener() {
    viewBinding.editButton.setOnClickListener {
      openEditItem()
    }
    viewBinding.completeButton.setOnClickListener {
      complete()
    }
    viewBinding.shareButton.setOnClickListener {
      shareUrl()
    }
    viewBinding.webButton.setOnClickListener {
      openWeb()
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

  private fun openWeb() {
    val intent = Intent(this, WebViewActivity::class.java).apply {
      putExtra(BOOKMARK_URL, viewModel.bookmark.value?.url)
    }
    startActivityForResult(intent, AddEditBookmarkActivity.REQUEST_CODE)
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
  }

  private fun openEditItem() {
    val intent = Intent(this, AddEditBookmarkActivity::class.java).apply {
      putExtra(AddEditBookmarkFragment.ARGUMENT_EDIT_ID, viewModel.bookmarkId)
    }
    startActivityForResult(intent, AddEditBookmarkActivity.REQUEST_CODE)
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
  }

  private fun shareUrl() {
    val sendIntent: Intent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT, viewModel.bookmark.value?.url)
      type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
  }

  private fun complete() {
    val intent = Intent(this, BookmarkActivity::class.java).apply {
      putExtra(
        AddEditBookmarkActivity.CATEGORY_ID,
        viewModel.categoryId
      )
    }
    setResult(ADD_EDIT_RESULT_OK, intent)

    finish()
  }

  companion object {
    const val EXTRA_BOOKMARK_ID = "BOOKMARK_ID"
    const val BOOKMARK_URL = "BOOKMARK_URL"
  }
}