package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.TvRepository
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvResponse
import javax.inject.Inject

class GetTvUseCase @Inject constructor(private val repository: TvRepository) {

    suspend operator fun invoke(page: Int): TvResponse {
        return repository.getPopularTv(page)
    }

    suspend fun getTvDetail(tvId: Int): TvDetail {
        return repository.getTvDetail(tvId)
    }
}