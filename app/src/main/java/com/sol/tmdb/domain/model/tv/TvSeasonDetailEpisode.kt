package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class TvSeasonDetailEpisode(
    @SerializedName("air_date") val airDate: String,
    val crew: List<TvCrew>,
    @SerializedName("episode_number") val episodeNumber: Int,
    @SerializedName("episode_type") val episodeType: String,
    @SerializedName("guest_stars") val guestStars: List<TvCast>,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("production_code") val productionCode: String,
    val runtime: Int,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("show_id") val showId: Int,
    @SerializedName("still_path") val stillPath: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)