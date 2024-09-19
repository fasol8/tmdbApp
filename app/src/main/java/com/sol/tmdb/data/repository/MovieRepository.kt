package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.movie.CountryResult
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieImagesResponse
import com.sol.tmdb.domain.model.movie.MovieNowResponse
import com.sol.tmdb.domain.model.movie.MovieProviderResponse
import com.sol.tmdb.domain.model.movie.MovieRecommendationResponse
import com.sol.tmdb.domain.model.movie.MovieRelease
import com.sol.tmdb.domain.model.movie.MovieResponse
import com.sol.tmdb.domain.model.movie.MovieSimilarResponse
import com.sol.tmdb.domain.model.movie.MovieVideosResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getDiscoverMovie(page: Int = 1): MovieResponse {
        return api.getDiscoverMovie(page)
    }

    suspend fun getNowPlaying(page: Int = 1): MovieNowResponse {
        return api.getNowPlayingMovie(page)
    }

    suspend fun getPopularMovie(page: Int = 1): MovieResponse {
        return api.getPopularMovie(page)
    }

    suspend fun getTopRated(page: Int = 1): MovieResponse {
        return api.getTopRatedMovie(page)
    }

    suspend fun getUpcoming(page: Int): MovieNowResponse {
        return api.getUpcomingMovie(page)
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        return api.getMovieDetail(movieId)
    }

    suspend fun getMovieRelease(movieId: Int): MovieRelease {
        return api.getMovieRelease(movieId)
    }

    suspend fun getMovieVideos(movieId: Int): MovieVideosResponse {
        return api.getMovieVideos(movieId)
    }

    suspend fun getMovieImages(movieId: Int): MovieImagesResponse {
        return api.getMovieImages(movieId)
    }

    suspend fun getMovieCredits(movieId: Int): MovieCredits {
        return api.getMovieCredits(movieId)
    }

    suspend fun getProvidersForMxAndUs(movieId: Int): Map<String, CountryResult> {
        val response = api.getMovieProviders(movieId)
        return response.results
    }

    suspend fun getMovieSimilar(movieId: Int): MovieSimilarResponse {
        return api.getMovieSimilar(movieId)
    }

    suspend fun getMovieRecommendation(movieId: Int): MovieRecommendationResponse {
        return api.getMovieRecommendation(movieId)
    }
}