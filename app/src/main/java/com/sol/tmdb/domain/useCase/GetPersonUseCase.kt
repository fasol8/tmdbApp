package com.sol.tmdb.domain.useCase

import com.sol.tmdb.data.repository.PersonRepository
import com.sol.tmdb.domain.model.person.PersonDetail
import com.sol.tmdb.domain.model.person.PersonResponse
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(private val repository: PersonRepository) {

    suspend operator fun invoke(page: Int): PersonResponse {
        return repository.getPopularPerson(page)
    }

    suspend fun getPersonDetail(personId: Int): PersonDetail {
        return repository.getPersonDetail(personId)
    }
}