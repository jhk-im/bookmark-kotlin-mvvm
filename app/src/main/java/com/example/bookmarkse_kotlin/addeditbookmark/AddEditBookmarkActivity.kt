package com.example.bookmarkse_kotlin.addeditbookmark

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.util.setupActionBar
import com.example.bookmarkse_kotlin.util.obtainViewModel
import com.example.bookmarkse_kotlin.util.replaceFragmentInActivity
import androidx.lifecycle.Observer
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.bookmark.BookmarkActivity
import com.example.bookmarkse_kotlin.bookmarkdetail.BookmarkDetailActivity
import com.example.bookmarkse_kotlin.util.ADD_EDIT_RESULT_OK

class AddEditBookmarkActivity : AppCompatActivity(), AddEditBookmarkNavigator {

    private lateinit var viewModel: AddEditBookmarkViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_bookmark_act)

        setupActionBar(R.id.toolbar) {
            // setHomeAsUpIndicator(R.drawable.ic_menu)
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

        if(viewModel.isNewItem) {
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