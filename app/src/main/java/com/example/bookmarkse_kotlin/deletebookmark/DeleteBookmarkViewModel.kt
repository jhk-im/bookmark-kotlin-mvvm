package com.example.bookmarkse_kotlin.deletebookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.bookmark.BookmarkFilterType
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository

class DeleteBookmarkViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    private val _bookmarks = MutableLiveData<List<Bookmark>>().apply { value = emptyList() }
    val bookmarks: LiveData<List<Bookmark>>
        get() = _bookmarks

    private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _currentCategory = MutableLiveData<String>()
    val currentCategory: LiveData<String>
        get() = _currentCategory

    private val _isCategoriesSetup = MutableLiveData<Boolean>()
    val isCategoriesSetup: LiveData<Boolean>
        get() = _isCategoriesSetup

    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    internal fun clickedCategory(categoryId: String) {
        _currentCategory.value = categoryId
        _isCategoriesSetup.value = false
        loadItems(false, showLoadingUI = false)
    }

    fun start() {
        loadItems(false)
    }

    fun loadItems(forceUpdate: Boolean) {
        loadItems(forceUpdate, true)
    }

    private fun loadItems(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            _dataLoading.value = true
        }

        if (forceUpdate) {
            itemsRepository.refreshBookmark()
        }

        itemsRepository.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {

                val bookmarksToShow = ArrayList<Bookmark>()
                val categoriesToShow = ArrayList<Category>()

                for (category in categories) {
                    if (_currentCategory.value == null && categories.indexOf(category) == 0)
                        _currentCategory.value = category.id

                    var isBookmarkEmpty = true
                    for (bookmark in bookmarks) {
                        if (bookmark.categoryId == category.id)
                            isBookmarkEmpty = false
                    }
                    if (isBookmarkEmpty) {
                        itemsRepository.deleteCategory(category.id)
                    } else {
                        categoriesToShow.add(category)
                    }
                }

                for (bookmark in bookmarks) {
                    if (bookmark.categoryId == currentCategory.value)
                        bookmarksToShow.add(bookmark)
                }

                if (showLoadingUI) {
                    _dataLoading.value = false
                }
                isDataLoadingError.value = false

                val bookmarksValue = ArrayList(bookmarksToShow)
                _bookmarks.value = bookmarksValue
                val categoriesValue = ArrayList(categoriesToShow)
                _categories.value = categoriesValue

                _isCategoriesSetup.value = true
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.value = true
                _dataLoading.value = false
            }
        })
    }

}