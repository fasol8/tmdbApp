package com.sol.tmdb.domain.model.tv

data class TvResponse(
    val page: Int,
    val results: List<TvResult>,
    val total_pages: Int,
    val total_results: Int
)