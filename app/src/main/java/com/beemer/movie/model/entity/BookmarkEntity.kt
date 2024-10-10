package com.beemer.movie.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val movieGenre: String
)