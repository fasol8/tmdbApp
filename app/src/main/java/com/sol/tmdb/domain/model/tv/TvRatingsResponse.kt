package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class TvRatingsResponse(
    val results: List<RatingInfo>,
    val id: Int
)

data class RatingInfo(
    val descriptors: List<String>,
    @SerializedName("iso_3166_1") val iso31661: String,
    val rating: String
)