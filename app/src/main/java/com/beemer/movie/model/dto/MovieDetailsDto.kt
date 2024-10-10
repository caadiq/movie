package com.beemer.movie.model.dto

class MovieDetailsDto(
    val movieCode: String,
    val movieName: String,
    val movieNameEn: String?,
    val openDate: String?,
    val posterUrl: String?,
    val genres: List<String>,
    val runTime: Int?,
    val nation: String?,
    val grade: String?,
    val plot: String?
)