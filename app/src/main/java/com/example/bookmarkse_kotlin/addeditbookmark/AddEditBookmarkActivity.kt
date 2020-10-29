package com.example.bookmarkse_kotlin.addeditbookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.util.setupActionBar
import com.example.bookmarkse_kotlin.util.obtainViewModel
import com.example.bookmarkse_kotlin.util.replaceFragmentInActivity
import androidx.lifecycle.Observer
import com.example.bookmarkse_kotlin.util.ADD_EDIT_RESULT_OK

class AddEditBookmarkActivity : AppCompatActivity(), AddEditBookmarkNavigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_bookmark_act)

        setupActionBar(R.id.toolbar) {
            // setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.edit_bookmark)
        }

        replaceFragmentInActivity(obtainViewFragment(), R.id.content_frame)

        obtainViewModel().itemUpdatedEvent.observe(this, Observer {
            this@AddEditBookmarkActivity.onItemSaved()
        })
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

    override fun onItemSaved() {
        setResult(ADD_EDIT_RESULT_OK)
        finish()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    companion object {

        const val REQUEST_CODE = 1
    }
}