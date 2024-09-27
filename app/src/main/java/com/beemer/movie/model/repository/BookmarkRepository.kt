package com.beemer.movie.model.repository

import com.beemer.movie.model.dao.BookmarkDao
import com.beemer.movie.model.entity.BookmarkEntity

class BookmarkRepository(private val dao: BookmarkDao) {
    fun getAllBookmark() = dao.getAllBookmark()

    suspend fun insertBookmark(dto: BookmarkEntity) = dao.insertBookmark(dto)

    suspend fun deleteAllBookmark() = dao.deleteAllBookmark()
}