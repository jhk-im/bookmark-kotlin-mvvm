package com.example.bookmarkse_kotlin.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.note.NoteActivity
import com.example.bookmarkse_kotlin.notice.NoticeActivity
import com.google.android.material.navigation.NavigationView
import com.example.bookmarkse_kotlin.util.setupActionBar

class HomeActivity : AppCompatActivity(), HomeNavigator {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_act)

        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.home_title)
        }

        setupNavigationDrawer()
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
        when(item.itemId) {
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
