package com.sol.tmdb.domain.model.movie

data class MovieCredits(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)