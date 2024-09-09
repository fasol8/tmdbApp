package com.sol.tmdb.domain.model.tv

import com.google.gson.annotations.SerializedName

data class TvProviderResponse(
    val id: Int,
    val results: Map<String, CountryResult>
)

data class CountryResult(
    val link: String,
    val flatrate: List<FlatrateProvider>
)

data class FlatrateProvider(
    @SerializedName("logo_path") val logoPath: String,
    @SerializedName("provider_id") val providerId: Int,
    @SerializedName("provider_name") val providerName: String,
    @SerializedName("display_priority") val displayPriority: Int
)