package com.beemer.movie.model.dto

data class SearchListDto(
    val page: PageDto,
    val count: CountDto,
    val movies: List<SearchList>
)

data class SearchList(
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val genre: String,
    val grade: String?,
    val openDate: String?
)