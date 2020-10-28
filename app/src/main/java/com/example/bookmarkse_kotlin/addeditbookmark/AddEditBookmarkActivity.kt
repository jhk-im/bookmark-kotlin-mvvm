package com.example.bookmarkse_kotlin.addeditbookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookmarkse_kotlin.R

class AddEditBookmarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_bookmark_act)
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}