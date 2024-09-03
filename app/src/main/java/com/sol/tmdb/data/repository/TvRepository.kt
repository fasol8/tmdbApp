package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.tv.TvResponse
import javax.inject.Inject

class TvRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getPopularTv(page: Int = 1): TvResponse {
        return api.getPopularTv(page)
    }
}