package com.sol.tmdb.domain.model.movie

data class MovieVideosResponse(
    val id: Int,
    val results: List<MovieVideosResult>
)