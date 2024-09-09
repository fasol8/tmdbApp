package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class SimilarResponse(
    val page: Int,
    val results: List<SimilarResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResult: Int
)