package com.sol.tmdb.domain.model.tv

data class TvVideosResponse(
    val id: Int,
    val results: List<TvVideosResult>
)