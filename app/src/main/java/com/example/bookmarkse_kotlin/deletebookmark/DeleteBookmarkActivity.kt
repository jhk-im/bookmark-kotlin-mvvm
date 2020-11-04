package com.example.bookmarkse_kotlin.deletebookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.bookmark.BookmarkFragment
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel
import com.example.bookmarkse_kotlin.util.obtainViewModel
import com.example.bookmarkse_kotlin.util.replaceFragmentInActivity
import com.example.bookmarkse_kotlin.util.setupActionBar

class DeleteBookmarkActivity : AppCompatActivity() {

    private lateinit var viewModel: DeleteBookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.delete_bookmark_act)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.delete_bookmark)
        }

        setupFragment()

        viewModel = obtainViewModel()
    }

    fun obtainViewModel(): DeleteBookmarkViewModel =
        obtainViewModel(DeleteBookmarkViewModel::class.java, this)


    private fun setupFragment() {
        supportFragmentManager.findFragmentById(R.id.content_frame)
            ?: replaceFragmentInActivity(DeleteBookmarkFragment.newInstance(), R.id.content_frame)
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

    companion object {

        const val REQUEST_CODE = 1
    }
}