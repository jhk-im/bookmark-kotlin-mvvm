package com.example.bookmarkse_kotlin.home

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookmarkse_kotlin.Injection
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.BookmarkDataSource
import com.example.bookmarkse_kotlin.data.source.BookmarkRepository
import com.example.bookmarkse_kotlin.note.NoteActivity
import com.example.bookmarkse_kotlin.notice.NoticeActivity
import com.google.android.material.navigation.NavigationView
import com.example.bookmarkse_kotlin.util.setupActionBar
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), HomeNavigator {

    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var bookmarkRepository: BookmarkRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_act)

        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.home_title)
        }

        setupNavigationDrawer()

        testLocalDatabase()
    }

    private fun testLocalDatabase() {
        bookmarkRepository = Injection.provideBookmarksRepository(this)

        val selectedAt = Date()
        // val newCategory = Category("Portal")
        val newBookmark =
            Bookmark("Google", "https://www.google.com", selectedAt)
        bookmarkRepository.saveBookmark(newBookmark)

        val selectedAt2 = Date()
        // val newCategory2 = Category("Portal")
        val newBookmark2 =
            Bookmark("Naver", "https://www.naver.com", selectedAt2)
        bookmarkRepository.saveBookmark(newBookmark2)

        val selectedAt3 = Date()
        // val newCategory3 = Category("Portal")
        val newBookmark3 =
            Bookmark("Daum", "https://www.daum.com", selectedAt3)
        bookmarkRepository.saveBookmark(newBookmark3)

        val sd = SimpleDateFormat("HH:mm:ss.SSS")

        bookmarkRepository.getBookmarks(object : BookmarkDataSource.LoadBookmarksCallback {

            override fun onBookmarksLoaded(bookmarks: List<Bookmark>) {
                for (bookmark in bookmarks) {
                    Log.e(
                        "bookmark",
                        bookmark.id + "\n" +
                                bookmark.title + "\n" +
                                bookmark.url + "\n" +
                                sd.format(bookmark.selectedAt) + "\n"
                    )
                }
            }

            override fun onDataNotAvailable() {
                //
            }
        })

        bookmarkRepository.deleteAllBookmarks()
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
                R.id.navigation_menu_home -> {
                    //
                }
                R.id.navigation_menu_notice -> {
                    val intent = Intent(this@HomeActivity, NoticeActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                }
                R.id.navigation_menu_note -> {
                    val intent = Intent(this@HomeActivity, NoteActivity::class.java).apply {
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
        TODO("Not yet implemented")
    }
}
