package com.beemer.movie.model.dto

data class PageDto(
    val previousPage: Int?,
    val currentPage: Int,
    val nextPage: Int?,
)