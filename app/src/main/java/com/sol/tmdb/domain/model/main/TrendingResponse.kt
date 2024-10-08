package com.sol.tmdb.domain.model.main

import com.google.gson.annotations.SerializedName

data class TrendingResponse(
    val page: Int,
    val results: List<TrendingResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResult: Int
)