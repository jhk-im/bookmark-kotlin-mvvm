package com.example.bookmarkse_kotlin.bookmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.databinding.BookmarkFragBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BookmarkFragment : Fragment() {

    private lateinit var viewDataBinding: BookmarkFragBinding
    private lateinit var bookmarkAdapter: BookmarkAdapter

    private lateinit var fab1: FloatingActionButton
    private lateinit var fab2: FloatingActionButton
    private lateinit var fab3: FloatingActionButton
    private lateinit var floatingScreen: FrameLayout
    var isFabClicked = false

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
            fab1.isHovered = false
            floatingScreen.visibility = View.GONE
            fab2.hide()
            fab3.hide()
            isFabClicked = false
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

    @SuppressLint("ResourceAsColor")
    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_items)?.let {
            fab1 = it
            fab1.setOnClickListener {
                // viewDataBinding.viewModel?.addNewBookmarks()
                isFabClicked = if (!isFabClicked) {
                    fab1.isHovered = true
                    floatingScreen.visibility = View.VISIBLE
                    fab2.show()
                    fab3.show()
                    true
                } else {
                    fab1.isHovered = false
                    floatingScreen.visibility = View.GONE
                    fab2.hide()
                    fab3.hide()
                    false
                }
            }
        }
        activity?.findViewById<FloatingActionButton>(R.id.fab_add_items)?.let {
            fab2 = it
            fab2.hide()
            fab2.setOnClickListener {
                viewDataBinding.viewModel?.addNewBookmarks()
                fab1.isHovered = false
                floatingScreen.visibility = View.GONE
                fab2.hide()
                fab3.hide()
                isFabClicked = false
            }
        }
        activity?.findViewById<FloatingActionButton>(R.id.fab_delete_items)?.let {
            fab3 = it
            fab3.hide()
            fab3.setOnClickListener {
                viewDataBinding.viewModel?.deleteBookmarks()
                fab1.isHovered = false
                floatingScreen.visibility = View.GONE
                fab2.hide()
                fab3.hide()
                isFabClicked = false
            }
        }
        activity?.findViewById<FrameLayout>(R.id.floating_screen_frame)?.let {
            floatingScreen = it
            floatingScreen.visibility = View.GONE
            floatingScreen.setOnClickListener {
                fab1.isHovered = false
                floatingScreen.visibility = View.GONE
                fab2.hide()
                fab3.hide()
                isFabClicked = false
            }
        }
    }

    companion object {
        fun newInstance() = BookmarkFragment()
        private const val TAG = "BookmarkFragment"
    }
}