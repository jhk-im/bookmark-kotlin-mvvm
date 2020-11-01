package com.example.bookmarkse_kotlin.bookmarkdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.util.DETAIL_RESULT_OK

class BookmarkDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookmark_detail_act)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(DETAIL_RESULT_OK)
        finish()
    }

    companion object {
        const val EXTRA_BOOKMARK_ID = "BOOKMARK_ID"
    }
}