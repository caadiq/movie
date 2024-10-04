package com.beemer.movie.model.api

import com.beemer.movie.model.dto.RankListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("/api/movie/rank/daily")
    fun getDailyRankList(@Query("date") date: String): Call<List<RankListDto>>

    @GET("/api/movie/rank/weekly")
    fun getWeeklyRankList(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Call<List<RankListDto>>
}