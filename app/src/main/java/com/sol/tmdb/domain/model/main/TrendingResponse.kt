package com.sol.tmdb.domain.model.main

data class TrendingResponse(
    val page: Int,
    val results: List<TrendingResult>,
    val total_pages: Int,
    val total_results: Int
)