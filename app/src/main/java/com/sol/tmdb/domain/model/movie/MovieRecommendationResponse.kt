package com.sol.tmdb.domain.model.movie

import com.google.gson.annotations.SerializedName

data class MovieRecommendationResponse(
    val page: Int,
    val results: List<MovieRecommendationResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)