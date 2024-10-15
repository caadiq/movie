package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.beemer.movie.model.entity.BookmarkEntity
import com.beemer.movie.model.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repository: BookmarkRepository) : ViewModel() {
    val bookmark: Flow<PagingData<BookmarkEntity>> = repository.getAllBookmark().cachedIn(viewModelScope)

    private val _isBookmarkExists = MutableLiveData<Boolean>()
    val isBookmarkExists: LiveData<Boolean> get() = _isBookmarkExists

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult

    private val _deleteAllResult = MutableLiveData<Boolean>()
    val deleteAllResult: LiveData<Boolean> get() = _deleteAllResult

    fun checkBookmarkExists(movieCode: String) {
        viewModelScope.launch {
            _isBookmarkExists.value = repository.getBookmarkByCode(movieCode) != null
        }
    }

    fun insertBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            try {
                repository.insertBookmark(bookmark)
                _insertResult.value = true
            } catch (e: Exception) {
                _insertResult.value = false
            }
        }
    }

    fun deleteBookmarkByCode(movieCode: String) {
        viewModelScope.launch {
            try {
                repository.deleteBookmarkByCode(movieCode)
                _deleteResult.value = true
            } catch (e: Exception) {
                _deleteResult.value = false
            }
        }
    }

    fun deleteAllBookmark() {
        viewModelScope.launch {
            try {
                repository.deleteAllBookmark()
                _deleteAllResult.value = true
            } catch (e: Exception) {
                _deleteAllResult.value = false
            }
        }
    }
}