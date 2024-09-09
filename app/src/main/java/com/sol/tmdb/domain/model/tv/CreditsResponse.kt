package com.sol.tmdb.domain.model.tv

data class CreditsResponse(
    val cast: List<TvCast>,
    val crew: List<TvCrew>,
    val id: Int
)