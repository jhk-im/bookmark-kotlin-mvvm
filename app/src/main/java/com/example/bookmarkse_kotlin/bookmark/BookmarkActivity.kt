package com.example.bookmarkse_kotlin.bookmark

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.addeditbookmark.AddEditBookmarkActivity
import com.example.bookmarkse_kotlin.bookmarkdetail.BookmarkDetailActivity
import com.example.bookmarkse_kotlin.deletebookmark.DeleteBookmarkActivity
import com.example.bookmarkse_kotlin.util.obtainViewModel
import com.example.bookmarkse_kotlin.util.replaceFragmentInActivity
import com.example.bookmarkse_kotlin.util.setupActionBar
import com.google.android.material.navigation.NavigationView
import android.util.Pair as UtilPair

class BookmarkActivity : AppCompatActivity(), BookmarkNavigator, BookmarkItemNavigator {

    private lateinit var mDrawerLayout: DrawerLayout

    private lateinit var viewModel: BookmarkViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.bookmark_act)

        setupActionBar(R.id.toolbar) {
            // setHomeAsUpIndicator(R.drawable.ic_menu)
            // setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.bookmark)
        }

        // setupNavigationDrawer()
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

//    private fun setupNavigationDrawer() {
//        mDrawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout)).apply {
//            setStatusBarBackground(R.color.colorPrimaryDark)
//        }
//        setupDrawerContent(findViewById(R.id.navigation_view))
//    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_menu_bookmark -> {
                    //
                }
                R.id.navigation_menu_notice -> {
                    //
                }
            }
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val categoryId = data?.getStringExtra(AddEditBookmarkActivity.CATEGORY_ID)
        viewModel.handleActivityResult(requestCode, resultCode, categoryId)
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
