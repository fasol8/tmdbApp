package com.sol.tmdb.presentation.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.person.ImagesProfile
import com.sol.tmdb.domain.model.person.MovieCreditsResponse
import com.sol.tmdb.domain.model.person.PersonDetail
import com.sol.tmdb.domain.model.person.PersonResult
import com.sol.tmdb.domain.model.person.TvCreditsResponse
import com.sol.tmdb.domain.useCase.GetPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(private val getPersonUseCase: GetPersonUseCase) :
    ViewModel() {

    private val _persons = MutableLiveData<List<PersonResult>>(emptyList())
    val persons: LiveData<List<PersonResult>> = _persons

    private val _personById = MutableLiveData<PersonDetail?>()
    val personById: LiveData<PersonDetail?> = _personById

    private val _creditsMovies = MutableLiveData<MovieCreditsResponse?>()
    val creditsMovies: LiveData<MovieCreditsResponse?> = _creditsMovies

    private val _creditsTv = MutableLiveData<TvCreditsResponse?>()
    val creditsTv: LiveData<TvCreditsResponse?> = _creditsTv

    private val _personImages = MutableLiveData<List<ImagesProfile>>(emptyList())
    val personImages: LiveData<List<ImagesProfile>> = _personImages

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1
    private var lastQuery: String? = null
    private var isLoading = false

    init {
        loadPerson()
    }

    fun searchAll(personId: Int) {
        searchPersonById(personId)
        searchCreditsMovies(personId)
        searchCreditsTv(personId)
        searchImagesProfile(personId)
    }

    fun loadPerson() {
        viewModelScope.launch {
            try {
                val response = getPersonUseCase(currentPage)
                val newPersons = response.results
                if (newPersons.isNotEmpty()) {
                    val updatePersons = _persons.value.orEmpty() + newPersons
                    _persons.value = updatePersons
                    currentPage++
                } else {
                    _errorMessage.value = "NO more persons"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"

            }
        }
    }

    fun searchPerson(query: String) {
        if (isLoading) return
        viewModelScope.launch {
            try {
                isLoading = true
                if (query.isBlank()) {
                    _persons.value = emptyList()
                    loadPerson()
                } else {
                    if (query != lastQuery) {
                        lastQuery = query
                        currentPage = 1
                        _persons.value = emptyList()
                    }
                    val response = getPersonUseCase.getSearchPerson(query, currentPage)
                    val newMovies = response.results
                    if (newMovies.isNotEmpty()) {
                        val updateMovie = _persons.value.orEmpty() + newMovies
                        _persons.value = updateMovie
                        currentPage++
                    } else {
                        _errorMessage.value = "No more results"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun searchPersonById(personId: Int) {
        viewModelScope.launch {
            try {
                val response = getPersonUseCase.getPersonDetail(personId)
                _personById.value = response
            } catch (e: Exception) {
                _personById.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchCreditsMovies(personId: Int) {
        viewModelScope.launch {
            try {
                val response = getPersonUseCase.getCreditsMovies(personId)
                _creditsMovies.value = response
            } catch (e: Exception) {
                _creditsMovies.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchCreditsTv(personId: Int) {
        viewModelScope.launch {
            try {
                val response = getPersonUseCase.getCreditsTv(personId)
                _creditsTv.value = response
            } catch (e: Exception) {
                _creditsTv.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchImagesProfile(personId: Int) {
        viewModelScope.launch {
            try {
                val response = getPersonUseCase.getImagesProfile(personId)
                _personImages.value = response.profiles
            } catch (e: Exception) {
                _personImages.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}