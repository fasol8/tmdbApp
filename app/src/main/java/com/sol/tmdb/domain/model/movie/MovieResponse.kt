package com.sol.tmdb.domain.model.movie

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    val results: List<MovieResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResult: Int
)