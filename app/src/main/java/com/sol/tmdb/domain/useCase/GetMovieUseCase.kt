package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.MovieRepository
import com.sol.tmdb.domain.model.movie.Certification
import com.sol.tmdb.domain.model.movie.CountryResult
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieRecommendationResponse
import com.sol.tmdb.domain.model.movie.MovieResponse
import com.sol.tmdb.domain.model.movie.MovieSimilarResponse
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(page: Int): MovieResponse {
        return repository.getDiscoverMovie(page)
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