package com.example.bookmarkse_kotlin.bookmark

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.databinding.BookmarkFragBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BookmarkFragment : Fragment() {

    private lateinit var viewDataBinding: BookmarkFragBinding
    private lateinit var listAdapter: BookmarkAdapter

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
        Log.e("0","resume")
        viewDataBinding.viewModel?.start()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_recent -> {
                //viewDataBinding.viewModel?
                true
            }
            R.id.menu_category -> {
                //viewDatBinding.vewModel?
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_tasks, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.setLifecycleOwner(this.viewLifecycleOwner)
        setUpListAdapter()
        setupFab()
    }

    private fun setUpListAdapter() {
        val viewModel = viewDataBinding.viewModel
        if (viewModel != null) {
            listAdapter = BookmarkAdapter(ArrayList(0), viewModel)
            viewDataBinding.bookmarksList.adapter = listAdapter
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