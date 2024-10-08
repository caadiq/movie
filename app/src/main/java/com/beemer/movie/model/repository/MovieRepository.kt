package com.beemer.movie.model.repository

import com.beemer.movie.model.api.MovieApi
import retrofit2.Retrofit
import retrofit2.awaitResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(retrofit: Retrofit) {
    private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)

    suspend fun getPosterBanner() = movieApi.getPosterBanner().awaitResponse().body() ?: emptyList()

    suspend fun getDailyRankList(date: String) = movieApi.getDailyRankList(date).awaitResponse().body()

    suspend fun getWeeklyRankList(startDate: String, endDate: String) = movieApi.getWeeklyRankList(startDate, endDate).awaitResponse().body()

    suspend fun getRecentReleaseList(limit: Int) = movieApi.getRecentReleaseList(limit).awaitResponse().body() ?: emptyList()

    suspend fun getComingReleaseList(limit: Int) = movieApi.getComingReleaseList(limit).awaitResponse().body() ?: emptyList()

    suspend fun getSearchList(page: Int?, limit: Int?, query: String) = movieApi.getSearchList(page, limit, query).awaitResponse().body()
}