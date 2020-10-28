package com.example.bookmarkse_kotlin.bookmark

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.ViewModelFactory
import com.example.bookmarkse_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Injection
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository
import com.example.bookmarkse_kotlin.notice.NoticeActivity
import com.example.bookmarkse_kotlin.util.obtainViewModel
import com.example.bookmarkse_kotlin.util.replaceFragmentInActivity
import com.example.bookmarkse_kotlin.util.setupActionBar
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*

class BookmarkActivity : AppCompatActivity(), BookmarkNavigator {

    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var mViewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookmark_act)

        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.home_title)
        }

        setupNavigationDrawer()
        setupFragment()

        mViewModel = obtainViewModel().apply {
            newBookmarkEvent.observe(this@BookmarkActivity, Observer<Event<Unit>> { event ->
                event.getContentIfNotHandled()?.let{
                    this@BookmarkActivity.addNewItem()
                }
            })
        }
    }

    fun obtainViewModel(): BookmarkViewModel =
        obtainViewModel(BookmarkViewModel::class.java,this)


    private fun setupFragment() {
        supportFragmentManager.findFragmentById(R.id.content_frame)
            ?: replaceFragmentInActivity(BookmarkFragment.newInstance(), R.id.content_frame)
    }

    private fun setupNavigationDrawer() {
        mDrawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        setupDrawerContent(findViewById(R.id.navigation_view))
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_menu_bookmark -> {
                    //
                }
                R.id.navigation_menu_notice -> {
                    val intent = Intent(this@BookmarkActivity, NoticeActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                }
            }
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun addNewItem() {
        val intent = Intent(this, AddEditBookmarkActivity::class.java)
        startActivityForResult(intent, AddEditBookmarkActivity.REQUEST_CODE)
    }

}
