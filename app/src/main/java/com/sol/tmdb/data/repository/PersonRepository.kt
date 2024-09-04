package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.person.PersonResponse
import javax.inject.Inject

class PersonRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getPopularPerson(page: Int = 1): PersonResponse {
        return api.getPopularPerson(page)
    }
}