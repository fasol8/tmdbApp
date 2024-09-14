package com.sol.tmdb.domain.model.movie

data class MovieImagesResponse(
    val backdrops: List<MovieImagesBackdrop>,
    val id: Int,
    val logos: List<MovieImagesLogo>,
    val posters: List<MovieImagesPoster>
)