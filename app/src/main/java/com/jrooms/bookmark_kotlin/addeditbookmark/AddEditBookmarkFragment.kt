/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jrooms.bookmark_kotlin.addeditbookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.jrooms.bookmark_kotlin.R
import com.jrooms.bookmark_kotlin.databinding.AddEditBookmarkFragBinding
import com.jrooms.bookmark_kotlin.util.setupSnackbar
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
      if (it.toString() == "") {
        viewDataBinding.viewModel?.categoryCheck(null)
      } else {
        viewDataBinding.viewModel?.categoryCheck(it.toString())
      }
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