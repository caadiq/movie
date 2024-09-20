package com.beemer.movie.model.repository

import com.beemer.movie.model.api.MovieApi
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(retrofit: Retrofit) {
    private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)

    suspend fun getDailyRankList(date: String) = movieApi.getDailyRankList(date).awaitResponse().body() ?: emptyList()

    suspend fun getWeeklyRankList(startDate: String, endDate: String) = movieApi.getWeeklyRankList(startDate, endDate).awaitResponse().body() ?: emptyList()
}