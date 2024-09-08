package com.sol.tmdb.data.repository

import com.sol.tmdb.data.network.TmdbApi
import com.sol.tmdb.domain.model.person.MovieCreditsResponse
import com.sol.tmdb.domain.model.person.PersonDetail
import com.sol.tmdb.domain.model.person.PersonResponse
import javax.inject.Inject

class PersonRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getPopularPerson(page: Int = 1): PersonResponse {
        return api.getPopularPerson(page)
    }

    suspend fun getPersonDetail(personId: Int): PersonDetail {
        return api.getPersonDetail(personId)
    }

    suspend fun getCreditsMovies(personId: Int): MovieCreditsResponse {
        return api.getCreditsMovies(personId)
    }
}