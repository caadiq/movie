package com.beemer.movie.model.dto

data class DailyRankListDto(
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val date: String,
    val rank: Int,
    val rankIncrement: Int,
    val audiCount: Int,
    val audiIncrement: Int,
    val audiAccumulate: Int
)