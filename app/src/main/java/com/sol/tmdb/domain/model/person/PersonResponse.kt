package com.sol.tmdb.domain.model.person

import com.google.gson.annotations.SerializedName

data class PersonResponse(
    val page: Int,
    val results: List<PersonResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)