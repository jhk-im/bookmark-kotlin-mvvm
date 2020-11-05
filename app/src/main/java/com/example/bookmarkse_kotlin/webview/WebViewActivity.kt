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
package com.example.bookmarkse_kotlin.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.bookmarkdetail.BookmarkDetailActivity
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.Injection
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import com.example.bookmarkse_kotlin.util.setupActionBar
import com.google.android.material.snackbar.Snackbar

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var settings: WebSettings
    private lateinit var urlEditText: EditText
    private lateinit var hideEditText: EditText
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var actionBar: ActionBar

    private lateinit var itemsRepository: ItemsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_act)

        itemsRepository = Injection.provideBookmarkRepository(application.applicationContext)

        val url = intent.getStringExtra(BookmarkDetailActivity.BOOKMARK_URL).toString()
        nestedScrollView = findViewById(R.id.nested_scroll)
        hideEditText = findViewById(R.id.hide_et)
        urlEditText = findViewById(R.id.et_url)
        urlEditText.setText(url)
        webView = findViewById(R.id.web_view)

        setupToolbar()
        setupWebView()
        setupEditText()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.web_view_menu, menu)
        return true
    }

    @SuppressLint("ShowToast")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
                true
            }
            R.id.web_view_menu_close -> {
                finish()
                true
            }
            R.id.web_view_menu_refresh -> {
                setupWebView()
                true
            }
            R.id.web_view_menu_bookmark -> {
                if (actionBar.title == "loading...") {
                    Snackbar.make(
                        findViewById(R.id.web_view_container),
                        "loading...",
                        Snackbar.LENGTH_SHORT
                    ).run {
                        show()
                    }
                } else {
                    addBookmark()
                }
                true
            }
            R.id.web_view_menu_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, urlEditText.text.toString())
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }
            else -> false
        }
    }

    private fun setupToolbar() {
        setupActionBar(R.id.active_toolbar) {
            setDisplayHomeAsUpEnabled(false)
        }

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            actionBar = supportActionBar!!
        }
    }

    private fun addBookmark() {
        val newCategory = Category("Bookmarks")
        itemsRepository.saveCategory(newCategory)

        val newBookmark = Bookmark(actionBar.title.toString(), urlEditText.text.toString()).apply {
            favicon = hideEditText.text.toString()
        }
        itemsRepository.saveBookmark(
            newCategory.title,
            newBookmark,
            object : ItemsDataSource.GetCategoryCallback {
                override fun onCategoryLoaded(categoryId: String) {
                    Snackbar.make(
                        findViewById(R.id.web_view_container),
                        "Add to bookmark",
                        Snackbar.LENGTH_SHORT
                    ).run {
                        show()
                    }
                }

                override fun onDataNotAvailable() {
                    Snackbar.make(
                        findViewById(R.id.web_view_container),
                        "Failed add to bookmark",
                        Snackbar.LENGTH_SHORT
                    ).run {
                        show()
                    }
                }
            })
    }

    private fun setupEditText() {
        urlEditText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                setupWebView()
            }
            false
        }

        urlEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val nothing = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nothing = null
            }

            override fun afterTextChanged(s: Editable?) {
                if (urlEditText.text.toString() == "") {
                    urlEditText.setText(R.string.http)
                    urlEditText.setSelection(urlEditText.text.length)
                }
            }

        })

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView() {
        webView.webViewClient = CustomWebClient(actionBar, nestedScrollView, urlEditText, hideEditText)
        settings = webView.settings
        settings.javaScriptEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.domStorageEnabled = true
        settings.builtInZoomControls = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.loadUrl(urlEditText.text.toString())
        nestedScrollView.y = 0f
        urlEditText.clearFocus()
    }


    class CustomWebClient(
        private val actionBar: ActionBar,
        private val scrollView: NestedScrollView,
        private val urlEdit: EditText,
        private val hideEdit: EditText
    ) : WebViewClient(

    ) {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            actionBar.setTitle(R.string.loading)
            urlEdit.setText(view?.url)
            view?.refreshDrawableState()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            actionBar.title = view?.title
            hideEdit.setText(view?.favicon.toString())
            scrollView.y = 0f
            view?.refreshDrawableState()
        }
    }
}