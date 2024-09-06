package com.sol.tmdb.domain.model.movie

import com.google.gson.annotations.SerializedName

data class MovieSimilarResponse(
    val page: Int,
    val results: List<MovieSimilarResult>,
   @SerializedName("total_pages") val totalPages: Int,
   @SerializedName("total_results") val totalResults: Int
)