package com.beemer.movie.model.api

import com.beemer.movie.model.dto.MovieDetailsDto
import com.beemer.movie.model.dto.PosterBannerDto
import com.beemer.movie.model.dto.RankListDto
import com.beemer.movie.model.dto.ReleaseListDto
import com.beemer.movie.model.dto.SearchListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/api/movie/poster/banner")
    fun getPosterBanner(): Call<List<PosterBannerDto>>

    @GET("/api/movie/rank/daily")
    fun getDailyRankList(@Query("date") date: String): Call<RankListDto>

    @GET("/api/movie/rank/weekly")
    fun getWeeklyRankList(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Call<RankListDto>

    @GET("/api/movie/release/recent")
    fun getRecentReleaseList(@Query("limit") limit: Int): Call<List<ReleaseListDto>>

    @GET("/api/movie/release/coming")
    fun getComingReleaseList(@Query("limit") limit: Int): Call<List<ReleaseListDto>>

    @GET("/api/movie/search")
    fun getSearchList(@Query("page") page: Int?, @Query("limit") limit: Int?, @Query("query") query: String): Call<SearchListDto>

    @GET("/api/movie/details")
    fun getMovieDetails(@Query("code") code: String): Call<MovieDetailsDto>
}