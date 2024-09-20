package com.beemer.movie.model.dto

data class WeeklyRankListDto(
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val startDate: String,
    val endDate: String,
    val rank: Int,
    val rankIncrement: Int,
    val audiCount: Int,
    val audiIncrement: Int,
    val audiAccumulate: Int
)
