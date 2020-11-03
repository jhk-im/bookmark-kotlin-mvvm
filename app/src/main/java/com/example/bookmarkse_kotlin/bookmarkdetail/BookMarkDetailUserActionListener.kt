package com.example.bookmarkse_kotlin.bookmarkdetail

interface BookMarkDetailUserActionListener {

    fun openWeb()
    fun openEditItem(bookmarkId: String)
    fun shareUrl()
}