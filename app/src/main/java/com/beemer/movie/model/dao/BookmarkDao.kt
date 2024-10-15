package com.beemer.movie.model.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.beemer.movie.model.entity.BookmarkEntity

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark ORDER BY id DESC")
    fun getAllBookmark(): PagingSource<Int, BookmarkEntity>

    @Query("SELECT * FROM bookmark WHERE movieCode = :code LIMIT 1")
    suspend fun getBookmarkByCode(code: String): BookmarkEntity?

    @Insert
    suspend fun insertBookmark(dto: BookmarkEntity)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmark()

    @Query("DELETE FROM bookmark WHERE movieCode = :code")
    suspend fun deleteBookmarkByCode(code: String)
}