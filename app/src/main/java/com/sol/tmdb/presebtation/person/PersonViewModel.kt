package com.sol.tmdb.presebtation.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.person.PersonResult
import com.sol.tmdb.domain.useCase.GetPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(private val getPersonUseCase: GetPersonUseCase) :
    ViewModel() {

    private val _persons = MutableLiveData<List<PersonResult>>()
    val persons: LiveData<List<PersonResult>> = _persons

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1

    init {
        loadPerson()
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
}