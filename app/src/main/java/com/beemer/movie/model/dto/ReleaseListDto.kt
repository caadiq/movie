package com.beemer.movie.model.dto

data class ReleaseListDto(
    val page: PageDto,
    val count: CountDto,
    val movies: List<ReleaseList>
)

data class ReleaseList(
    val movieCode: String,
    val movieName: String,
    val posterUrl: String,
    val releaseDate: String?
)