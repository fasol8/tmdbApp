package com.sol.tmdb.domain.model.person

import com.google.gson.annotations.SerializedName

data class PersonDetail(
    val adult: Boolean,
    @SerializedName("also_known_as") val alsoKnownAs: List<String>,
    val biography: String,
    val birthday: String,
    @SerializedName("deathday") val deathDay: Any,
    val gender: Int,
    val homepage: Any,
    val id: Int,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("known_for_department") val knownForDepartment: String,
    val name: String,
    @SerializedName("place_of_birth") val placeOfBirth: String,
    val popularity: Double,
    @SerializedName("profile_path") val profilePath: String
)