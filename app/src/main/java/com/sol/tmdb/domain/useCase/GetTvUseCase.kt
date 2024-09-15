package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.TvRepository
import com.sol.tmdb.domain.model.tv.CountryResult
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResponse
import com.sol.tmdb.domain.model.tv.EpisodesImagesResponse
import com.sol.tmdb.domain.model.tv.TvCertification
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvImagesResponse
import com.sol.tmdb.domain.model.tv.TvRecommendationsResponse
import com.sol.tmdb.domain.model.tv.TvResponse
import com.sol.tmdb.domain.model.tv.TvSeasonDetailResponse
import com.sol.tmdb.domain.model.tv.TvVideosResponse
import javax.inject.Inject

class GetTvUseCase @Inject constructor(private val repository: TvRepository) {

    suspend operator fun invoke(page: Int): TvResponse {
        return repository.getPopularTv(page)
    }

    suspend fun getTvDetail(tvId: Int): TvDetail {
        return repository.getTvDetail(tvId)
    }

    suspend fun getTcRatingsByCountry(tvId: Int): Map<String, TvCertification?> {
        val tvRatings = repository.getRatings(tvId)
        val filterCountries =
            tvRatings.results.filter { it.iso31661 == "MX" || it.iso31661 == "US" }

        return filterCountries.associate { country ->
            val countryRating = country.rating
            country.iso31661 to TvCertification.fromCertification(countryRating, country.iso31661)
        }
    }

    suspend fun getTvCredits(tvId: Int): CreditsResponse {
        return repository.getTvCredits(tvId)
    }

    suspend fun getTvProvidersForMxAndUsUseCase(tvId: Int): Map<String, CountryResult?> {
        val providers = repository.getTvProvidersForMxAndUs(tvId)
        return mapOf(
            "MX" to providers["MX"],
            "US" to providers["US"]
        )
    }

    suspend fun getTvImages(tvId: Int): TvImagesResponse {
        return repository.getTvImages(tvId)
    }

    suspend fun getTvVideos(tvId: Int):TvVideosResponse{
        return repository.getTvVideos(tvId)
    }
    suspend fun getTVSimilar(tvId: Int): SimilarResponse {
        return repository.getTvSimilar(tvId)
    }

    suspend fun getTvRecommendation(tvId: Int): TvRecommendationsResponse {
        return repository.getTvRecommendations(tvId)
    }

    suspend fun getTvSeasonDetails(tvId: Int, seasonNumber: Int): TvSeasonDetailResponse {
        return repository.getSeasonDetails(tvId, seasonNumber)
    }

    suspend fun getTvImagesEpisode(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodesImagesResponse {
        return repository.getImagesEpisode(tvId, seasonNumber, episodeNumber)
    }
}