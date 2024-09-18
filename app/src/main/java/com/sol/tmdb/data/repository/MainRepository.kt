package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.main.SearchResponse
import com.sol.tmdb.domain.model.main.TrendingResponse
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getTrending(type: String, time: String): TrendingResponse {
        return api.getTrending(type, time)
    }

    suspend fun getMultiSearch(query:String, page:Int):SearchResponse{
        return api.getMultiSearch(query, page)
    }
}