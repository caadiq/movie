package com.beemer.movie.model.dto

class MovieDetailsDto(
    val details: Details,
    val trend: List<Trend>
)

data class Details(
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

data class Trend(
    val date: String,
    val rank: Int,
    val rankIncrement: Int,
    val audiCount: Int,
    val audiChangeRate: Float,
    val audiAccumulate: Int
)