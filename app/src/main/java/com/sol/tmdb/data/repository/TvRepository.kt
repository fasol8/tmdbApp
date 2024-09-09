package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResponse
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvRecommendationsResponse
import com.sol.tmdb.domain.model.tv.TvResponse
import javax.inject.Inject

class TvRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getPopularTv(page: Int = 1): TvResponse {
        return api.getPopularTv(page)
    }

    suspend fun getTvDetail(tvId: Int): TvDetail {
        return api.getTvDetail(tvId)
    }

    suspend fun getTvCredits(tvId: Int): CreditsResponse {
        return api.getTvCredits(tvId)
    }

    suspend fun getTvSimilar(tvId: Int): SimilarResponse {
        return api.getTvSimilar(tvId)
    }

    suspend fun getTvRecommendations(tvId: Int):TvRecommendationsResponse{
        return api.getTvRecommendations(tvId)
    }
}