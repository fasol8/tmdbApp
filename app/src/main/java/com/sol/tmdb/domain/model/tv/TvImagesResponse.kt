package com.sol.tmdb.domain.model.tv

data class TvImagesResponse(
    val backdrops: List<TvImagesBackdrop>,
    val id: Int,
    val logos: List<TvImagesLogo>,
    val posters: List<TvImagesPoster>
)