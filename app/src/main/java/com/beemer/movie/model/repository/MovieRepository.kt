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

    suspend fun getReleaseList(page: Int?, limit: Int?, type: String) = movieApi.getReleaseList(page, limit, type).awaitResponse().body()

    suspend fun getRecentReleaseList() = movieApi.getReleaseList(0, 5, "recent").awaitResponse().body()

    suspend fun getComingReleaseList() = movieApi.getReleaseList(0, 5, "coming").awaitResponse().body()

    suspend fun getSearchList(page: Int?, limit: Int?, query: String) = movieApi.getSearchList(page, limit, query).awaitResponse().body()

    suspend fun getMovieDetails(code: String) = movieApi.getMovieDetails(code).awaitResponse().body()
}