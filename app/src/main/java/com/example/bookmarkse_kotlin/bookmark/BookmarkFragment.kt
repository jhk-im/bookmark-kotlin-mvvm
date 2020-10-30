package com.example.bookmarkse_kotlin.bookmark

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.databinding.BookmarkFragBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BookmarkFragment : Fragment() {

    private lateinit var viewDataBinding: BookmarkFragBinding
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = BookmarkFragBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as BookmarkActivity).obtainViewModel()
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewModel?.start()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_filter -> {
                setUpFilteringPopUpMenu()
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bookmarks_fragment_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setUpFilteringPopUpMenu()
        setUpListAdapter()
        setupFab()
    }

    private fun setUpFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                viewDataBinding.viewModel?.run {
                    setFiltering(
                        when (it.itemId) {
                            R.id.menu_recent -> BookmarkFilterType.RECENT_BOOKMARKS
                            else -> BookmarkFilterType.CATEGORY_BOOKMARKS
                        }
                    )
                    loadItems(false)
                }
                true
            }
            show()
        }
    }

    private fun setUpListAdapter() {
        val viewModel = viewDataBinding.viewModel
        if (viewModel != null) {
            bookmarkAdapter = BookmarkAdapter(ArrayList(0), viewModel)
            viewDataBinding.bookmarksList.adapter = bookmarkAdapter
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_add_items)?.let {
            it.setImageResource(R.drawable.ic_add)
            it.setOnClickListener {
                viewDataBinding.viewModel?.addNewBookmarks()
            }
        }
    }

    companion object {
        fun newInstance() = BookmarkFragment()
        private const val TAG = "BookmarkFragment"
    }
}