package com.beemer.movie.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beemer.movie.model.dao.BookmarkDao
import com.beemer.movie.model.dao.SearchHistoryDao
import com.beemer.movie.model.entity.BookmarkEntity
import com.beemer.movie.model.entity.SearchHistoryEntity

@Database(entities = [SearchHistoryEntity::class, BookmarkEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun bookmarkDao(): BookmarkDao
}