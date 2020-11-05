package com.example.bookmarkse_kotlin.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.ScrollChildSwipeRefreshLayout
import com.example.bookmarkse_kotlin.bookmark.BookmarkViewModel
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object: Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.decrement()
            }
        })

    }
}

fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}

@BindingAdapter("android:onRefresh")
fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
    viewModel: BookmarkViewModel ) {
    setOnRefreshListener { viewModel.loadItems(true) }
}