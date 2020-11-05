package com.example.bookmarkse_kotlin.deletebookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.bookmark.BookmarkActivity
import com.example.bookmarkse_kotlin.bookmark.BookmarkAdapter
import com.example.bookmarkse_kotlin.bookmark.BookmarkFragment
import com.example.bookmarkse_kotlin.databinding.BookmarkFragBinding
import com.example.bookmarkse_kotlin.databinding.DeleteBookmarkFragBinding
import com.example.bookmarkse_kotlin.util.setupSnackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class DeleteBookmarkFragment : Fragment() {

    private lateinit var viewBinding: DeleteBookmarkFragBinding
    private lateinit var bookmarkAdapter: DeleteBookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DeleteBookmarkFragBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as DeleteBookmarkActivity).obtainViewModel()
        }
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewBinding.viewModel?.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        viewBinding.viewModel?.let {
            view?.setupSnackbar(this,it.snackbarMessage, Snackbar.LENGTH_SHORT)
        }
        setupFab()
        setUpListAdapter()
        setUpSelectAll()
    }

    private fun setUpSelectAll() {
        viewBinding.selectAllCb.isSelected = false
        viewBinding.selectAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    viewBinding.viewModel?.selectAllBookmark(true)
                }
                false -> {
                    viewBinding.viewModel?.selectAllBookmark(false)
                }
            }
        }
    }

    private fun setUpListAdapter() {
        val viewModel = viewBinding.viewModel
        if (viewModel != null) {
            bookmarkAdapter = DeleteBookmarkAdapter(ArrayList(0), viewModel)
            viewBinding.bookmarksList.adapter = bookmarkAdapter
        } else {
            Log.w(DeleteBookmarkFragment.TAG, "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_done)?.let {
            it.setImageResource(R.drawable.ic_done)
            it.setOnClickListener { viewBinding.viewModel?.deleteBookmarks() }
        }
    }

    companion object {
        fun newInstance() = DeleteBookmarkFragment()
        private const val TAG = "DeleteBookmarkFragment"
    }
}