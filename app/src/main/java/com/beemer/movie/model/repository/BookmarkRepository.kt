package com.beemer.movie.model.repository

import com.beemer.movie.model.dao.BookmarkDao
import com.beemer.movie.model.entity.BookmarkEntity

class BookmarkRepository(private val dao: BookmarkDao) {
    fun getAllBookmark() = dao.getAllBookmark()

    suspend fun getBookmarkByCode(code: String) = dao.getBookmarkByCode(code)

    suspend fun insertBookmark(dto: BookmarkEntity) = dao.insertBookmark(dto)

    suspend fun deleteAllBookmark() = dao.deleteAllBookmark()

    suspend fun deleteBookmarkByCode(code: String) = dao.deleteBookmarkByCode(code)
}