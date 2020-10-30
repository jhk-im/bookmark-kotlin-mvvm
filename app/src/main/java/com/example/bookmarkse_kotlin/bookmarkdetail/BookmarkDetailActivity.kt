package com.example.bookmarkse_kotlin.bookmarkdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookmarkse_kotlin.R

class BookmarkDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookmark_detail_activity)


    }

    companion object {

        const val EXTRA_BOOKMARK_ID = "BOOKMARK_ID"

    }
}