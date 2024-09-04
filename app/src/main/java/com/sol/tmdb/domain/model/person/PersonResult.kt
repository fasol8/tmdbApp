package com.sol.tmdb.domain.model.person

import com.google.gson.annotations.SerializedName

data class PersonResult(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    @SerializedName("known_for") val knownFor: List<KnownFor>,
    @SerializedName("known_for_department") val knownForDepartment: String,
    val name: String,
    @SerializedName("original_name") val originalName: String,
    val popularity: Double,
    @SerializedName("profile_path") val profilePath: String
)