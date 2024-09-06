package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.MovieRepository
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

    suspend fun getMovieCredits(movieId: Int): MovieCredits {
        return repository.getMovieCredits(movieId)
    }

    suspend fun getMovieSimilar(movieId: Int): MovieSimilarResponse {
        return repository.getMovieSimilar(movieId)
    }

    suspend fun getMovieRecommendation(movieId: Int): MovieRecommendationResponse {
        return repository.getMovieRecommendation(movieId)
    }
}