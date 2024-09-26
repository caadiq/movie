package com.beemer.movie.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beemer.movie.model.dao.SearchHistoryDao
import com.beemer.movie.model.entity.SearchHistoryEntity

@Database(entities = [SearchHistoryEntity::class], version = 1)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}