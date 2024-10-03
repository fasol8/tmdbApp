package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.MovieRepository
import com.sol.tmdb.data.repository.db.movie.MovieEntity
import com.sol.tmdb.domain.model.movie.Certification
import com.sol.tmdb.domain.model.movie.CountryResult
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieImagesResponse
import com.sol.tmdb.domain.model.movie.MovieNowResponse
import com.sol.tmdb.domain.model.movie.MovieRecommendationResponse
import com.sol.tmdb.domain.model.movie.MovieResponse
import com.sol.tmdb.domain.model.movie.MovieSimilarResponse
import com.sol.tmdb.domain.model.movie.MovieVideosResponse
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(page: Int, language: String): MovieResponse {
        return repository.getDiscoverMovie(page, language)
    }

    suspend fun getNowPlaying(page: Int = 1, language: String): MovieNowResponse {
        return repository.getNowPlaying(page, language)
    }

    suspend fun getPopularMovie(page: Int = 1, language: String): MovieResponse {
        return repository.getPopularMovie(page, language)
    }

    suspend fun getTopRated(page: Int = 1, language: String): MovieResponse {
        return repository.getTopRated(page, language)
    }

    suspend fun getUpcoming(page: Int, language: String): MovieNowResponse {
        return repository.getUpcoming(page, language)
    }

    suspend fun getSearchMovie(query: String, page: Int = 1): MovieResponse {
        return repository.getSearchMovie(query, page)
    }

    suspend fun getMovieDetail(movieId: Int, language: String): MovieDetail {
        return repository.getMovieDetail(movieId, language)
    }

    suspend fun getMovieReleaseWithCertification(movieId: Int): Map<String?, Certification?> {
        val movieRelease = repository.getMovieRelease(movieId)
        val filteredCountries =
            movieRelease.results?.filter { it.iso31661 == "MX" || it.iso31661 == "US" }

        return filteredCountries!!.associate { country ->
            val certification = country.releaseDates?.firstOrNull()?.certification
            country.iso31661 to Certification.fromCountryAndCertification(
                country.iso31661!!,
                certification!!
            )
        }
    }

    suspend fun getMovieVideos(movieId: Int, language: String): MovieVideosResponse {
        return repository.getMovieVideos(movieId, language)
    }

    suspend fun getMovieImages(movieId: Int): MovieImagesResponse {
        return repository.getMovieImages(movieId)
    }

    suspend fun getMovieCredits(movieId: Int): MovieCredits {
        return repository.getMovieCredits(movieId)
    }

    suspend fun getProvidersForMxAndUsUseCase(movieId: Int): Map<String, CountryResult?> {
        val providers = repository.getProvidersForMxAndUs(movieId)
        return mapOf(
            "MX" to providers["MX"],
            "US" to providers["US"]
        )
    }

    suspend fun getMovieSimilar(movieId: Int): MovieSimilarResponse {
        return repository.getMovieSimilar(movieId)
    }

    suspend fun getMovieRecommendation(movieId: Int): MovieRecommendationResponse {
        return repository.getMovieRecommendation(movieId)
    }

    suspend fun getFavoriteMovies(): List<MovieEntity> {
        return repository.getFavoriteMovies()
    }

    suspend fun getMovieById(movieId: Int): MovieEntity? {
        return repository.getMovieById(movieId)
    }

    suspend fun getWatchListMovies(): List<MovieEntity> {
        return repository.getWatchListMovies()
    }

    suspend fun updateMovie(movie: MovieEntity) {
        repository.updateMovie(movie)
    }

    suspend fun insertMovie(movie: MovieEntity) {
        repository.insertMovie(movie)
    }
}