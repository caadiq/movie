package com.beemer.movie.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.beemer.movie.model.entity.BookmarkEntity

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAllBookmark(): LiveData<List<BookmarkEntity>>

    @Insert
    suspend fun insertBookmark(dto: BookmarkEntity)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmark()
}