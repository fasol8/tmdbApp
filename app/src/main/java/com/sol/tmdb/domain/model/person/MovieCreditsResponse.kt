package com.sol.tmdb.domain.model.person

data class MovieCreditsResponse(
    val cast: List<MovieCast>,
    val crew: List<MovieCrew>,
    val id: Int
)