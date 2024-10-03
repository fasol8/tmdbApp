package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.data.repository.db.movie.MovieDao
import com.sol.tmdb.data.repository.db.movie.MovieEntity
import com.sol.tmdb.domain.model.movie.CountryResult
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieImagesResponse
import com.sol.tmdb.domain.model.movie.MovieNowResponse
import com.sol.tmdb.domain.model.movie.MovieRecommendationResponse
import com.sol.tmdb.domain.model.movie.MovieRelease
import com.sol.tmdb.domain.model.movie.MovieResponse
import com.sol.tmdb.domain.model.movie.MovieSimilarResponse
import com.sol.tmdb.domain.model.movie.MovieVideosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: TmdbApi,
    private val movieDao: MovieDao
) {

    suspend fun getDiscoverMovie(page: Int = 1, language: String): MovieResponse {
        return api.getDiscoverMovie(page, language)
    }

    suspend fun getNowPlaying(page: Int = 1, language: String): MovieNowResponse {
        return api.getNowPlayingMovie(page, language)
    }

    suspend fun getPopularMovie(page: Int = 1, language: String): MovieResponse {
        return api.getPopularMovie(page, language)
    }

    suspend fun getTopRated(page: Int = 1, language: String): MovieResponse {
        return api.getTopRatedMovie(page, language)
    }

    suspend fun getUpcoming(page: Int, language: String): MovieNowResponse {
        return api.getUpcomingMovie(page, language)
    }

    suspend fun getSearchMovie(query: String, page: Int = 1): MovieResponse {
        return api.getSearchMovie(query, page)
    }

    suspend fun getMovieDetail(movieId: Int, language: String): MovieDetail {
        return api.getMovieDetail(movieId, language)
    }

    suspend fun getMovieRelease(movieId: Int): MovieRelease {
        return api.getMovieRelease(movieId)
    }

    suspend fun getMovieVideos(movieId: Int, language: String): MovieVideosResponse {
        return api.getMovieVideos(movieId, language)
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

    suspend fun getMovieById(movieId: Int): MovieEntity? {
        return withContext(Dispatchers.IO) {
            movieDao.getMovieById(movieId)
        }
    }

    suspend fun getFavoriteMovies(): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            movieDao.getFavoriteMovies()
        }
    }

    suspend fun getWatchListMovies(): List<MovieEntity> {
        return withContext(Dispatchers.IO) {
            movieDao.getWatchListMovies()
        }
    }

    suspend fun updateMovie(movie: MovieEntity) {
        movieDao.updateMovie(movie)
    }

    suspend fun insertMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }
}