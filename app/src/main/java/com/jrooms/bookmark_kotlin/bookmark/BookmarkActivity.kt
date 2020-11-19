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
package com.jrooms.bookmark_kotlin.bookmark

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jrooms.bookmark_kotlin.Event
import com.jrooms.bookmark_kotlin.R
import com.jrooms.bookmark_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.jrooms.bookmark_kotlin.bookmarkdetail.BookmarkDetailActivity
import com.jrooms.bookmark_kotlin.deletebookmark.DeleteBookmarkActivity
import com.jrooms.bookmark_kotlin.util.obtainViewModel
import com.jrooms.bookmark_kotlin.util.replaceFragmentInActivity
import com.jrooms.bookmark_kotlin.util.setupActionBar
import android.util.Pair as UtilPair

class BookmarkActivity : AppCompatActivity(), BookmarkNavigator, BookmarkItemNavigator {

  private lateinit var viewModel: BookmarkViewModel

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    setContentView(R.layout.bookmark_act)

    setupActionBar(R.id.toolbar) {
      setTitle(R.string.bookmark)
    }

    setupFragment()

    viewModel = obtainViewModel().apply {
      newBookmarkEvent.observe(this@BookmarkActivity, Observer<Event<Unit>> { event ->
        event.getContentIfNotHandled()?.let {
          this@BookmarkActivity.addNewItem()
        }
      })

      deleteBookmarkEvent.observe(this@BookmarkActivity, Observer<Event<Unit>> { event ->
        event.getContentIfNotHandled()?.let {
          this@BookmarkActivity.deleteItem()
        }
      })

      openBookmarkEvent.observe(this@BookmarkActivity, Observer<Event<String>> { event ->
        event.getContentIfNotHandled()?.let {
          openBookmarkDetails(it)
        }
      })
    }
  }

  fun obtainViewModel(): BookmarkViewModel =
    obtainViewModel(BookmarkViewModel::class.java, this)


  private fun setupFragment() {
    supportFragmentManager.findFragmentById(R.id.content_frame)
      ?: replaceFragmentInActivity(BookmarkFragment.newInstance(), R.id.content_frame)
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    val categoryId = data?.getStringExtra(AddEditBookmarkActivity.CATEGORY_ID)
    viewModel.handleActivityResult(requestCode, resultCode, categoryId)
  }

  override fun addNewItem() {
    val intent = Intent(this, AddEditBookmarkActivity::class.java)
    startActivityForResult(intent, AddEditBookmarkActivity.REQUEST_CODE)
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
  }

  override fun deleteItem() {
    val intent = Intent(this, DeleteBookmarkActivity::class.java)
    startActivityForResult(intent, DeleteBookmarkActivity.REQUEST_CODE)
    overridePendingTransition(R.anim.fadein, R.anim.fadeout)
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  override fun openBookmarkDetails(bookmarkId: String) {
    val intent = Intent(this, BookmarkDetailActivity::class.java).apply {
      putExtra(BookmarkDetailActivity.EXTRA_BOOKMARK_ID, bookmarkId)
    }
    val options = ActivityOptions
      .makeSceneTransitionAnimation(
        this,
        UtilPair(viewModel.bookmarkImage.value, "transition_img"),
        UtilPair(viewModel.bookmarkTitle.value, "transition_title"),
        UtilPair(viewModel.bookmarkUrl.value, "transition_url")
      )
    //startActivity(intent, options.toBundle())
    startActivityForResult(
      intent,
      AddEditBookmarkActivity.REQUEST_CODE,
      options.toBundle()
    )
  }
}
