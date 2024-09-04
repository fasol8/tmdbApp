package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class NextEpisodeToAir(
    @SerializedName("air_date") val airDate: String,
    @SerializedName("episode_number") val episodeNumber: Int,
    @SerializedName("episode_type") val episodeType: String,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("production_code") val productionCode: String,
    val runtime: Any,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("show_id") val showId: Int,
    @SerializedName("still_path") val stillPath: Any,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)