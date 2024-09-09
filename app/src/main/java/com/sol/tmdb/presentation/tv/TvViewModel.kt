package com.sol.tmdb.presentation.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResult
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvRecommendationsResult
import com.sol.tmdb.domain.model.tv.TvResult
import com.sol.tmdb.domain.useCase.GetTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(private val getTvUseCase: GetTvUseCase) : ViewModel() {

    private val _tvs = MutableLiveData<List<TvResult>>()
    val tvs: LiveData<List<TvResult>> = _tvs

    private val _tvById = MutableLiveData<TvDetail?>()
    val tvById: LiveData<TvDetail?> = _tvById

    private val _tvCredits = MutableLiveData<CreditsResponse?>()
    val tvCredits: LiveData<CreditsResponse?> = _tvCredits

    private val _tvSimilar = MutableLiveData<List<SimilarResult?>>()
    val tvSimilar: LiveData<List<SimilarResult?>> = _tvSimilar

    private val _tvRecommendations = MutableLiveData<List<TvRecommendationsResult?>>()
    val tvRecommendations: LiveData<List<TvRecommendationsResult?>> = _tvRecommendations

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1

    init {
        loadTv()
    }

    fun loadTv() {
        viewModelScope.launch {
            try {
                val response = getTvUseCase(currentPage)
                val newTvs = response.results
                if (newTvs.isNotEmpty()) {
                    val updateTvs = _tvs.value.orEmpty() + newTvs
                    _tvs.value = updateTvs
                    currentPage++
                } else {
                    _errorMessage.value = "NO more tv shows"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchTvById(id: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvDetail(id)
                _tvById.value = response
            } catch (e: Exception) {
                _tvById.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchTvCredits(id: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvCredits(id)
                _tvCredits.value = response
            } catch (e: Exception) {
                _tvCredits.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchTvSimilar(id: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTVSimilar(id)
                _tvSimilar.value = response.results
            } catch (e: Exception) {
                _tvSimilar.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchTvRecommendation(id: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvRecommendation(id)
                _tvRecommendations.value = response.results
            } catch (e: Exception) {
                _tvRecommendations.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}