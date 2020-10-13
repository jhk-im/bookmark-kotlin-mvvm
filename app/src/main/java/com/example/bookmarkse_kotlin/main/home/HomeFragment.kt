package com.example.bookmarkse_kotlin.main.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.bookmarkse_kotlin.databinding.HomeFragBinding
import com.example.bookmarkse_kotlin.main.MainActivity

class HomeFragment : Fragment() {

    private lateinit var viewDataBinding: HomeFragBinding
    private lateinit var bookmarkAdapter: BookmarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = HomeFragBinding.inflate(inflater, container, false).apply {
            viewModel = (activity as MainActivity).obtainViewModel()
        }

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewModel?.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        fun newInstance() = HomeFragment()
        private const val TAG = "HomeFragment"

    }
}
