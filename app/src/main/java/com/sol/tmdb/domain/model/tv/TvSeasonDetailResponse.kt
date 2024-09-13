package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class TvSeasonDetailResponse(
    @SerializedName("_id") val seasonId: String,
    @SerializedName("air_date") val airDate: String,
    val episodes: List<TvSeasonDetailEpisode>,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("vote_average") val voteAverage: Double,
)