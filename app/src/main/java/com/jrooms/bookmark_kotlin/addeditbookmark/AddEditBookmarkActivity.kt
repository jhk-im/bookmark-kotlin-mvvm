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
package com.jrooms.bookmark_kotlin.addeditbookmark

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.jrooms.bookmark_kotlin.R
import com.jrooms.bookmark_kotlin.util.setupActionBar
import com.jrooms.bookmark_kotlin.util.obtainViewModel
import com.jrooms.bookmark_kotlin.util.replaceFragmentInActivity
import androidx.lifecycle.Observer
import com.jrooms.bookmark_kotlin.Event
import com.jrooms.bookmark_kotlin.bookmark.BookmarkActivity
import com.jrooms.bookmark_kotlin.bookmarkdetail.BookmarkDetailActivity
import com.jrooms.bookmark_kotlin.util.ADD_EDIT_RESULT_OK

class AddEditBookmarkActivity : AppCompatActivity(), AddEditBookmarkNavigator {

  private lateinit var viewModel: AddEditBookmarkViewModel

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.add_edit_bookmark_act)

    setupActionBar(R.id.toolbar) {
      setDisplayHomeAsUpEnabled(true)
      setTitle(R.string.edit_bookmark)
    }

    replaceFragmentInActivity(obtainViewFragment(), R.id.content_frame)

    viewModel = obtainViewModel().apply {
      itemUpdatedEvent.observe(this@AddEditBookmarkActivity, Observer<Event<String>> { event ->
        event.getContentIfNotHandled()?.let {
          onItemSaved(it)
        }
      })
    }
  }

  fun obtainViewModel(): AddEditBookmarkViewModel =
    obtainViewModel(AddEditBookmarkViewModel::class.java, this)

  private fun obtainViewFragment() = supportFragmentManager.findFragmentById(R.id.content_frame)
    ?: AddEditBookmarkFragment.newInstance().apply {
      arguments = Bundle().apply {
        putString(
          AddEditBookmarkFragment.ARGUMENT_EDIT_ID,
          intent.getStringExtra(AddEditBookmarkFragment.ARGUMENT_EDIT_ID)
        )
      }
    }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        finish()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun onBackPressed() {
    super.onBackPressed()

    finish()
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onItemSaved(categoryId: String) {

    if (viewModel.isNewItem) {
      val intent = Intent(this, BookmarkActivity::class.java).apply {
        putExtra(CATEGORY_ID, categoryId)
      }
      setResult(ADD_EDIT_RESULT_OK, intent)
    } else {
      val intent = Intent(this, BookmarkDetailActivity::class.java).apply {
        putExtra(CATEGORY_ID, categoryId)
      }
      setResult(ADD_EDIT_RESULT_OK, intent)
    }
    finish()
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
  }

  companion object {

    const val REQUEST_CODE = 1
    const val CATEGORY_ID = "CATEGORY_ID"
  }
}