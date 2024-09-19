package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.MovieRepository
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

    suspend operator fun invoke(page: Int): MovieResponse {
        return repository.getDiscoverMovie(page)
    }

    suspend fun getNowPlaying(page: Int = 1): MovieNowResponse {
        return repository.getNowPlaying(page)
    }

    suspend fun getPopularMovie(page: Int = 1): MovieResponse {
        return repository.getPopularMovie(page)
    }

    suspend fun getTopRated(page: Int = 1): MovieResponse {
        return repository.getTopRated(page)
    }

    suspend fun getUpcoming(page: Int): MovieNowResponse {
        return repository.getUpcoming(page)
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        return repository.getMovieDetail(movieId)
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

    suspend fun getMovieVideos(movieId: Int): MovieVideosResponse {
        return repository.getMovieVideos(movieId)
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
}