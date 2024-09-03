package com.sol.tmdb.data.network

import com.sol.tmdb.BuildConfig
import com.sol.tmdb.domain.model.movie.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("discover/movie")
    suspend fun getDiscoverMovie(
        @Query("page") page: Int = 1,
        @Query("api_key") apikey: String = BuildConfig.TMDB_API_KEY
    ): MovieResponse
}