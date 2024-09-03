package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.MovieRepository
import com.sol.tmdb.domain.model.movie.MovieResponse
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(page:Int): MovieResponse {
        return repository.getDiscoverMovie(page)
    }
}