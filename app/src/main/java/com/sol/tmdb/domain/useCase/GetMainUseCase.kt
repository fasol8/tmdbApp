package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.MainRepository
import com.sol.tmdb.domain.model.main.SearchResponse
import com.sol.tmdb.domain.model.main.TrendingResponse
import javax.inject.Inject

class GetMainUseCase @Inject constructor(private val repository: MainRepository) {

    suspend operator fun invoke(type: String, time: String): TrendingResponse {
        return repository.getTrending(type, time)
    }

    suspend fun getMultiSearch(query: String, page: Int): SearchResponse {
        return repository.getMultiSearch(query, page)
    }
}