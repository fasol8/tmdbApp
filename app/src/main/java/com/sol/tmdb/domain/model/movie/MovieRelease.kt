package com.sol.tmdb.domain.model.movie

import com.google.gson.annotations.SerializedName

data class MovieRelease(
    val id: Int?,
    val results: List<CountryRelease>?
)

data class CountryRelease(
    @SerializedName("iso_3166_1") val iso31661: String?,
    @SerializedName("release_dates") val releaseDates: List<ReleaseDate>?
)

data class ReleaseDate(
    val certification: String?,
    val descriptors: List<String>?,
    @SerializedName("iso_639_1") val iso6391: String?,
    val note: String?,
    @SerializedName("release_date") val releaseDate: String?,
    val type: Int?
)
