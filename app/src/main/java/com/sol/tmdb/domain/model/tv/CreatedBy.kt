package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("credit_id") val creditId: String,
    val gender: Int,
    val id: Int,
    val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("profile_path") val profilePath: Any
)