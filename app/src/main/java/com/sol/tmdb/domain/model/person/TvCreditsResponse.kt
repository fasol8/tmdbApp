package com.sol.tmdb.domain.model.person

data class TvCreditsResponse(
    val cast: List<TvCast>,
    val crew: List<TvCrew>,
    val id: Int
)