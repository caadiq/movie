package com.beemer.movie.model.dto

data class RankListDto(
    val date: Date,
    val rankList: List<RankList>
)

data class Date(
    val prevDate: String?,
    val currenetDate: String,
    val nextDate: String?
)

data class RankList(
    val movieCode: String,
    val movieName: String,
    val genre: String,
    val posterUrl: String,
    val openDate: String,
    val rank: Int,
    val rankIncrement: Int,
    val audiCount: Int,
    val audiIncrement: Int,
    val audiAccumulate: Int
)