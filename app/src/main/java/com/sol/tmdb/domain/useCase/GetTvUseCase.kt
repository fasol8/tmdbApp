package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.TvRepository
import com.sol.tmdb.data.repository.db.movie.MovieEntity
import com.sol.tmdb.data.repository.db.tv.TvEntity
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

    suspend operator fun invoke(page: Int, language: String): TvResponse {
        return repository.getDiscoverTv(page, language)
    }

    suspend fun getAirToday(page: Int, language: String): TvResponse {
        return repository.getAirTodayTv(page, language)
    }

    suspend fun getOnAir(page: Int, language: String): TvResponse {
        return repository.getOnAirTv(page, language)
    }

    suspend fun getPopularTv(page: Int, language: String): TvResponse {
        return repository.getPopularTv(page, language)
    }

    suspend fun getTopRatedTv(page: Int, language: String): TvResponse {
        return repository.getTopRatedTv(page, language)
    }

    suspend fun getSearchTv(query: String, page: Int = 1): TvResponse {
        return repository.getSearchTv(query, page)
    }

    suspend fun getTvDetail(tvId: Int, language: String): TvDetail {
        return repository.getTvDetail(tvId, language)
    }

    suspend fun getTvRatingsByCountry(tvId: Int): Map<String, TvCertification?> {
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

    suspend fun getTvVideos(tvId: Int): TvVideosResponse {
        return repository.getTvVideos(tvId)
    }

    suspend fun getTVSimilar(tvId: Int): SimilarResponse {
        return repository.getTvSimilar(tvId)
    }

    suspend fun getTvRecommendations(tvId: Int): TvRecommendationsResponse {
        return repository.getTvRecommendations(tvId)
    }

    suspend fun getTvSeasonDetails(
        tvId: Int,
        seasonNumber: Int,
        language: String
    ): TvSeasonDetailResponse {
        return repository.getSeasonDetails(tvId, seasonNumber, language)
    }

    suspend fun getTvImagesEpisode(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodesImagesResponse {
        return repository.getImagesEpisode(tvId, seasonNumber, episodeNumber)
    }

    suspend fun getFavoriteTvs(): List<TvEntity> {
        return repository.getFavoriteTvs()
    }

    suspend fun getTvById(tvId: Int): TvEntity? {
        return repository.getTvById(tvId)
    }

    suspend fun getWatchListTvs(): List<TvEntity> {
        return repository.getWatchListTvs()
    }

    suspend fun updateTv(tv: TvEntity) {
        repository.updateTv(tv)
    }

    suspend fun insertTv(tv: TvEntity) {
        repository.insertTv(tv)
    }
}