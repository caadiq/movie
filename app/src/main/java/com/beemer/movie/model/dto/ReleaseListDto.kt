package com.beemer.movie.model.dto

data class ReleaseListDto(
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val releaseDate: String?
)