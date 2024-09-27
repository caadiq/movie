package com.beemer.movie.viewmodel

import androidx.lifecycle.LiveData
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

    fun insertBookmark(bookmark: BookmarkEntity) {
        viewModelScope.launch {
            repository.insertBookmark(bookmark)
        }
    }

    fun deleteAllBookmark() {
        viewModelScope.launch {
            repository.deleteAllBookmark()
        }
    }
}