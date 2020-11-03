package com.example.bookmarkse_kotlin.addeditbookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.databinding.AddEditBookmarkFragBinding
import com.example.bookmarkse_kotlin.util.setupSnackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AddEditBookmarkFragment : Fragment() {

    private lateinit var viewDataBinding: AddEditBookmarkFragBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
        viewDataBinding.viewModel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.add_edit_bookmark_frag, container, false)
        viewDataBinding = AddEditBookmarkFragBinding.bind(root).apply {
            viewModel = (activity as AddEditBookmarkActivity).obtainViewModel()
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        retainInstance = false

        setupCategoryChangeListener()

        return viewDataBinding.root
    }

    private fun setupCategoryChangeListener() {
        viewDataBinding.categoryEt.addTextChangedListener {
            //Log.e("categoryET",it.toString())
            if(!viewDataBinding.viewModel?.isCategoryClicked!!)
            viewDataBinding.viewModel?.categoryCheck(it.toString())
        }
    }

    private fun loadData() {
        // Add or edit an existing task?
        viewDataBinding.viewModel?.start(arguments?.getString(ARGUMENT_EDIT_ID))
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_done)?.let {
            it.setImageResource(R.drawable.ic_done)
            it.setOnClickListener { viewDataBinding.viewModel?.saveItem() }
        }
    }

    companion object {
        const val ARGUMENT_EDIT_ID = "EDIT_BOOKMARK_ID"

        fun newInstance() = AddEditBookmarkFragment()
    }
}