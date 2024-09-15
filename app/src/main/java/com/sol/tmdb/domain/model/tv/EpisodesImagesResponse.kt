package com.sol.tmdb.domain.model.tv

data class EpisodesImagesResponse(
    val id: Int,
    val stills: List<TvImagesStill>
)