package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class TvCast(
    val adult: Boolean,
    val character: String,
    @SerializedName("credit_id") val creditId: String,
    val gender: Int,
    val id: Int,
    @SerializedName("known_for_department") val knownForDepartment: String,
    val name: String,
    val order: Int,
    @SerializedName("original_name") val originalName: String,
    val popularity: Double,
    @SerializedName("profile_path") val profilePath: String
)