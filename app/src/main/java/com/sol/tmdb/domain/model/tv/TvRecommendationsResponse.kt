package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class TvRecommendationsResponse(
    val page: Int,
    val results: List<TvRecommendationsResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResult: Int
)