package com.sol.tmdb.domain.model.main

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val page: Int,
    val results: List<SearchResult>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResult: Int
)