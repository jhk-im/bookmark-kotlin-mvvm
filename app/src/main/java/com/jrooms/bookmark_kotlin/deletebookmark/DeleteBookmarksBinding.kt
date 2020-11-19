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
package com.jrooms.bookmark_kotlin.deletebookmark

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jrooms.bookmark_kotlin.data.Bookmark

object DeleteBookmarksBinding {

  @BindingAdapter("app:deleteBookmarks")
  @JvmStatic
  fun setDeleteBookmarks(list: RecyclerView, @NonNull bookmarks: List<Bookmark>) {
    with(list.adapter as? DeleteBookmarkAdapter) {
      this?.replaceBookmarks(bookmarks)
    }
  }
}