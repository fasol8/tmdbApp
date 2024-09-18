package com.sol.tmdb.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.main.SearchResponse
import com.sol.tmdb.domain.model.main.SearchResult
import com.sol.tmdb.domain.model.main.TrendingResult
import com.sol.tmdb.domain.useCase.GetMainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getMainUseCase: GetMainUseCase) : ViewModel() {

    private val _trending = MutableLiveData<List<TrendingResult>>()
    val trending: LiveData<List<TrendingResult>> = _trending

    private val _searchResults = MutableLiveData<List<SearchResult>?>()
    val searchResults: LiveData<List<SearchResult>?> = _searchResults

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1
    private var isLoading = false
    private var lastQuery: String? = null

    init {
        loadTrending("all", "day")
    }

    fun loadTrending(type: String, time: String) {
        viewModelScope.launch {
            try {
                val response = getMainUseCase(type, time)
                _trending.value = response.results
            } catch (e: Exception) {
//                _trending.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchMultiByQuery(query: String) {
        if (lastQuery != query) {
            lastQuery = query
            currentPage = 1
            _searchResults.value = emptyList()
        }
        loadNextPage(query)
    }

    fun loadNextPage(query: String) {
        if (isLoading) return

        viewModelScope.launch {
            try {
                isLoading = true
                val response = getMainUseCase.getMultiSearch(query, currentPage)
                val newMulti = response.results
                if (newMulti.isNotEmpty()) {
                    val updateMulti = _searchResults.value.orEmpty() + newMulti
                    _searchResults.value = updateMulti
                    currentPage++
                } else {
                    _errorMessage.value = "No more results"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}