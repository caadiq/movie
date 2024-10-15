package com.beemer.movie.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.beemer.movie.model.dao.BookmarkDao
import com.beemer.movie.model.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

class BookmarkRepository(private val dao: BookmarkDao) {
    fun getAllBookmark(): Flow<PagingData<BookmarkEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getAllBookmark() }
        ).flow
    }

    suspend fun getBookmarkByCode(code: String) = dao.getBookmarkByCode(code)

    suspend fun insertBookmark(dto: BookmarkEntity) = dao.insertBookmark(dto)

    suspend fun deleteAllBookmark() = dao.deleteAllBookmark()

    suspend fun deleteBookmarkByCode(code: String) = dao.deleteBookmarkByCode(code)
}