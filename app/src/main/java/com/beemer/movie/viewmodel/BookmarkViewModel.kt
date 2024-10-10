package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.movie.model.entity.BookmarkEntity
import com.beemer.movie.model.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repository: BookmarkRepository) : ViewModel() {
    val bookmark: LiveData<List<BookmarkEntity>> = repository.getAllBookmark()

    private val _isBookmarkExists = MutableLiveData<Boolean>()
    val isBookmarkExists: LiveData<Boolean> get() = _isBookmarkExists

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> get() = _insertResult

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> get() = _deleteResult


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

    fun deleteAllBookmark() {
        viewModelScope.launch {
            repository.deleteAllBookmark()
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
}