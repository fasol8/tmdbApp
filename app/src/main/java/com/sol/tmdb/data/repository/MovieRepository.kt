package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getDiscoverMovie(page: Int = 1): MovieResponse {
        return api.getDiscoverMovie(page)
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        return api.getMovieDetail(movieId)
    }

    suspend fun getMovieCredits(movieId: Int): MovieCredits {
        return api.getMovieCredits(movieId)
    }
}