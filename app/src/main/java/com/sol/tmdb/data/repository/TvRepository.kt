package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.tv.CountryResult
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResponse
import com.sol.tmdb.domain.model.tv.EpisodesImagesResponse
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvImagesResponse
import com.sol.tmdb.domain.model.tv.TvRatingsResponse
import com.sol.tmdb.domain.model.tv.TvRecommendationsResponse
import com.sol.tmdb.domain.model.tv.TvResponse
import com.sol.tmdb.domain.model.tv.TvSeasonDetailResponse
import com.sol.tmdb.domain.model.tv.TvVideosResponse
import javax.inject.Inject

class TvRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getDiscoverTv(page: Int = 1, language: String): TvResponse {
        return api.getDiscoverTv(page, language)
    }

    suspend fun getAirTodayTv(page: Int = 1, language: String): TvResponse {
        return api.getAirTodayTv(page, language)
    }

    suspend fun getOnAirTv(page: Int = 1, language: String): TvResponse {
        return api.getOnAirTv(page, language)
    }

    suspend fun getPopularTv(page: Int = 1, language: String): TvResponse {
        return api.getPopularTv(page, language)
    }

    suspend fun getTopRatedTv(page: Int = 1, language: String): TvResponse {
        return api.getTopRatedTv(page, language)
    }

    suspend fun getSearchTv(query: String, page: Int = 1): TvResponse {
        return api.getSearchTv(query, page)
    }

    suspend fun getTvDetail(tvId: Int, language: String): TvDetail {
        return api.getTvDetail(tvId, language)
    }

    suspend fun getRatings(tvId: Int): TvRatingsResponse {
        return api.getTvRatings(tvId)
    }

    suspend fun getTvCredits(tvId: Int): CreditsResponse {
        return api.getTvCredits(tvId)
    }

    suspend fun getTvProvidersForMxAndUs(tvId: Int): Map<String, CountryResult> {
        val response = api.getTvProviders(tvId)
        return response.results
    }

    suspend fun getTvImages(tvId: Int): TvImagesResponse {
        return api.getTvImages(tvId)
    }

    suspend fun getTvVideos(tvId: Int): TvVideosResponse {
        return api.getTvVideos(tvId)
    }

    suspend fun getTvSimilar(tvId: Int): SimilarResponse {
        return api.getTvSimilar(tvId)
    }

    suspend fun getTvRecommendations(tvId: Int): TvRecommendationsResponse {
        return api.getTvRecommendations(tvId)
    }

    suspend fun getSeasonDetails(
        tvId: Int,
        seasonNumber: Int,
        language: String
    ): TvSeasonDetailResponse {
        return api.getTvSeasonDetails(tvId, seasonNumber, language)
    }

    suspend fun getImagesEpisode(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodesImagesResponse {
        return api.getTvImagesEpisode(tvId, seasonNumber, episodeNumber)
    }
}