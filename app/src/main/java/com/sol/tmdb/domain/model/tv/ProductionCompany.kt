package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    val id: Int,
    @SerializedName("logo_path") val logoPath: Any,
    val name: String,
    @SerializedName("origin_country") val originCountry: String
)