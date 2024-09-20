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

    suspend fun getDiscoverTv(page: Int = 1): TvResponse {
        return api.getDiscoverTv(page)
    }

    suspend fun getAirTodayTv(page: Int = 1): TvResponse {
        return api.getAirTodayTv(page)
    }

    suspend fun getOnAirTv(page: Int = 1): TvResponse {
        return api.getOnAirTv(page)
    }

    suspend fun getPopularTv(page: Int = 1): TvResponse {
        return api.getPopularTv(page)
    }

    suspend fun getTopRatedTv(page: Int = 1): TvResponse {
        return api.getTopRatedTv(page)
    }

    suspend fun getTvDetail(tvId: Int): TvDetail {
        return api.getTvDetail(tvId)
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

    suspend fun getSeasonDetails(tvId: Int, seasonNumber: Int): TvSeasonDetailResponse {
        return api.getTvSeasonDetails(tvId, seasonNumber)
    }

    suspend fun getImagesEpisode(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodesImagesResponse {
        return api.getTvImagesEpisode(tvId, seasonNumber, episodeNumber)
    }
}