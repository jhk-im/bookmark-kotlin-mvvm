package com.example.bookmarkse_kotlin.addeditbookmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookmarkse_kotlin.Event
import com.example.bookmarkse_kotlin.R
import com.example.bookmarkse_kotlin.data.Bookmark
import com.example.bookmarkse_kotlin.data.Category
import com.example.bookmarkse_kotlin.data.source.ItemsDataSource
import com.example.bookmarkse_kotlin.data.source.ItemsRepository

class AddEditBookmarkViewModel(
    private val itemsRepository: ItemsRepository
): ViewModel(), ItemsDataSource.GetBookmarkCallback {

    val title = MutableLiveData<String>()
    val url = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    private var bookmarkId: String? = null
    private var isNewItem: Boolean = false
    private var isDataLoaded = false

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() =_dataLoading

    private val _itemUpdated = MutableLiveData<Event<Unit>>()
    val itemUpdatedEvent: LiveData<Event<Unit>>
        get() = _itemUpdated

    fun start(bookmarkId: String?){

        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }

        this.bookmarkId = bookmarkId
        if (bookmarkId == null) {
            isNewItem = true
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
        title.value = book.title
        url.value = book.url
        //itemsRepository.getCategory
        _dataLoading.value = false
        isDataLoaded = true
    }

    override fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    internal fun saveItem() {
        val newCategory: Category?
        val newBookmark: Bookmark?

        if( title.value == null || url.value == null || category.value == null){
            _snackbarText.value = Event(R.string.empty_input_message)
        }

        newCategory = Category(category.value.toString())
        itemsRepository.saveCategory(newCategory)

        if (isNewItem || bookmarkId == null) {
            newBookmark = Bookmark(
                title.value.toString(),
                url.value.toString()
            )
        } else {

            newBookmark = Bookmark(
                title.value.toString(),
                url.value.toString(),
                bookmarkId.toString()
            )
        }
        itemsRepository.saveBookmark(category.value.toString(),newBookmark)
        _itemUpdated.value = Event(Unit)
    }

}