package com.sol.tmdb.data.network

import com.sol.tmdb.BuildConfig
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieResponse
import com.sol.tmdb.domain.model.tv.TvResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

interface TmdbApi {

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Query("page") page: Int = 1,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieDetail

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("page") page: Int = 1,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): TvResponse
}