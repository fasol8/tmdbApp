package com.sol.tmdb.domain.model.movie

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
   @SerializedName("iso_3166_1") val iso31661: String,
    val name: String
)