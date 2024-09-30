package com.beemer.movie.model.dto

data class ChartListDto(
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val openDate: String,
    val genre: String,
    val rank: Int,
    val rankIncrement: Int
)
