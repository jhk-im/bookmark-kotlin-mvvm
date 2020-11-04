package com.example.bookmarkse_kotlin.addeditbookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository
import java.util.*
import java.util.regex.Pattern

class AddEditBookmarkViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel(), ItemsDataSource.GetBookmarkCallback {

    val bookmarkTitle = MutableLiveData<String>()
    val urlAddress = MutableLiveData<String>()
    val categoryTitle = MutableLiveData<String>()

    private val _categories = MutableLiveData<List<Category>>().apply { value = emptyList() }
    val categories: LiveData<List<Category>>
        get() = _categories

    fun categoryCheck(input: String?) {
        var isNewCategory = true
        var categoryId = ""
        for (category in categories.value!!) {
            if (category.title == input) {
                isNewCategory = false
                categoryId = category.id
            }
        }
        categoryTitle.value = input

        if (isNewCategory) {
            onCategoriesLoaded(categoryId)
        } else {
            onCategoriesLoaded(categoryId)
        }
    }

    var bookmarkId: String? = null
    private var getFavicon: String? = null
    var isNewItem: Boolean = false
    private var isDataLoaded = false

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _itemUpdated = MutableLiveData<Event<String>>()
    val itemUpdatedEvent: LiveData<Event<String>>
        get() = _itemUpdated

    fun start(bookmarkId: String?) {

        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }

        this.bookmarkId = bookmarkId
        if (bookmarkId == null) {
            isNewItem = true
            onCategoriesLoaded("")
            return
        }

        if (isDataLoaded) {
            return
        }

        isNewItem = false
        _dataLoading.value = true

        itemsRepository.getBookmark(bookmarkId, this)
    }

    override fun onBookmarkLoaded(book: Bookmark) {
        bookmarkTitle.value = book.title
        urlAddress.value = book.url
        onCategoriesLoaded(book.categoryId)
        _dataLoading.value = false
        isDataLoaded = true
    }

    override fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    internal fun saveItem() {
        val newCategory: Category?
        val currentTitle = bookmarkTitle.value
        val currentUrl = urlAddress.value
        val currentCategory = categoryTitle.value

        if (currentTitle == null || currentUrl == null  || currentCategory == null) {
            _snackbarText.value = Event(R.string.empty_input_message)
            return
        }

        if (!checkUrlValidation()) {
            _snackbarText.value = Event(R.string.url_validation_check)
            return
        }

        newCategory = Category(currentCategory)
        itemsRepository.saveCategory(newCategory)

        val newBookmark: Bookmark? = if (isNewItem || bookmarkId == null) {
            Bookmark(
                currentTitle,
                currentUrl
            ).apply { favicon = getFavicon.toString() }
        } else {
            Bookmark(
                currentTitle,
                currentUrl,
                bookmarkId.toString()
            ).apply { favicon = getFavicon.toString() }
        }
        if (newBookmark != null) {
            itemsRepository.saveBookmark(
                currentCategory,
                newBookmark,
                object : ItemsDataSource.GetCategoryCallback {
                    override fun onCategoryLoaded(categoryId: String) {
                        _itemUpdated.value = Event(categoryId)
                    }

                    override fun onDataNotAvailable() {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    private fun onCategoriesLoaded(categoryId: String?) {
        itemsRepository.getItems(object : ItemsDataSource.LoadItemsCallback {
            override fun onItemsLoaded(bookmarks: List<Bookmark>, categories: List<Category>) {
                val categoriesToShow = ArrayList<Category>()
                _categories.value = emptyList()
                for (category in categories) {
                    var isBookmarkEmpty = true
                    for (bookmark in bookmarks){
                        if(bookmark.categoryId == category.id)
                            isBookmarkEmpty = false
                    }
                    if (isBookmarkEmpty){
                        itemsRepository.deleteCategory(category.id)
                    } else {
                        categoriesToShow.add(category)
                    }
                    if (categoryId == category.id) {
                        categoryTitle.value = category.title
                        Log.e("","${categoryTitle.value}")
                    }
                }
                _categories.value = categoriesToShow
                Log.e("","${categoryTitle.value}")
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }
        })
    }

    private fun checkUrlValidation(): Boolean {
        val checkUrl = urlAddress.value
        val regex =
            "^((http|https)://){1}([a-zA-Z0-9]+[.]{1})?([a-zA-Z0-9]+){1}[.]{1}[a-z]+([/]{1}[a-zA-Z0-9]*)*"
        val match = Objects.requireNonNull(checkUrl)!!.matches(regex.toRegex())
        val urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)((\\/[^\\s\\/]+)*)")

        return if (match) {
            val matcher = urlPattern.matcher(checkUrl)
            if (matcher.matches()) {
                getFavicon = matcher.group(1) + "://" + matcher.group(2) + "/favicon.ico"
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}