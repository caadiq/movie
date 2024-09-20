package com.beemer.movie.model.di

import com.beemer.movie.model.repository.MovieRepository
import com.beemer.movie.model.service.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = RetrofitService.getRetrofit()

    @Provides
    @Singleton
    fun provideMovieRepository(retrofit: Retrofit): MovieRepository = MovieRepository(retrofit)
}