package com.sol.tmdb.presentation.tv

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.tv.CountryResult
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResult
import com.sol.tmdb.domain.model.tv.TvCertification
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvImagesStill
import com.sol.tmdb.domain.model.tv.TvRecommendationsResult
import com.sol.tmdb.domain.model.tv.TvResult
import com.sol.tmdb.domain.model.tv.TvSeasonDetailResponse
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

    private val _tvRatings = MutableLiveData<Map<String, TvCertification?>?>()
    val tvRatings: LiveData<Map<String, TvCertification?>?> = _tvRatings

    private val _tvCredits = MutableLiveData<CreditsResponse?>()
    val tvCredits: LiveData<CreditsResponse?> = _tvCredits

    private val _tvProviders = MutableLiveData<Map<String, CountryResult?>>()
    val tvProviders: LiveData<Map<String, CountryResult?>> = _tvProviders

    private val _tvSimilar = MutableLiveData<List<SimilarResult?>>()
    val tvSimilar: LiveData<List<SimilarResult?>> = _tvSimilar

    private val _tvRecommendations = MutableLiveData<List<TvRecommendationsResult?>>()
    val tvRecommendations: LiveData<List<TvRecommendationsResult?>> = _tvRecommendations

    private val _seasonDetails = MutableLiveData<List<TvSeasonDetailResponse>>()
    val seasonDetails: LiveData<List<TvSeasonDetailResponse>> = _seasonDetails

    private val _episodeImages = MutableLiveData<Map<Int, List<TvImagesStill>>>()
    val episodeImages: LiveData<Map<Int, List<TvImagesStill>>> = _episodeImages

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1
    private val seasonDetailsList = mutableListOf<TvSeasonDetailResponse>()

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

    fun searchTvRatings(id: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTcRatingsByCountry(id)
                _tvRatings.value = response
            } catch (e: Exception) {
                _tvRatings.value = null
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

    fun searchTvProvidersForMXAndUs(id: Int) {
        viewModelScope.launch {
            try {
                val result = getTvUseCase.getTvProvidersForMxAndUsUseCase(id)
                _tvProviders.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Error loading providers"

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

    fun loadAllSeasons(tvId: Int, numberOfSeasons: Int) {
        viewModelScope.launch {
            try {
                val seasonZero = getTvUseCase.getTvSeasonDetails(tvId, 0)
                Log.i("VM", seasonZero.toString())
                seasonDetailsList.add(seasonZero)
                _seasonDetails.value = seasonDetailsList.toList()
            } catch (e: Exception) {
                _errorMessage.value = "Error loading season 0: ${e.message}"
                Log.i("Error", "Error loading season 0: ${e.message}")
            }
            for (seasonNumber in 1..numberOfSeasons) {
                try {
                    val response = getTvUseCase.getTvSeasonDetails(tvId, seasonNumber)
                    seasonDetailsList.add(response)
                    _seasonDetails.value = seasonDetailsList.toList()

                } catch (e: Exception) {
                    _errorMessage.value = "Error loading season $seasonNumber: ${e.message}"
                }
            }
        }
    }

    fun loadEpisodeImages(tvId: Int, seasonNumber: Int, numberOfEpisodes: Int) {
        viewModelScope.launch {
            val imagesMap = mutableMapOf<Int, List<TvImagesStill>>()
            try {
                for (episodeNumber in 1..numberOfEpisodes) {
                    val response = getTvUseCase.getTvImagesEpisode(tvId, seasonNumber, episodeNumber)
                    imagesMap[episodeNumber] = response.stills
                }
                _episodeImages.value = imagesMap
            } catch (e: Exception) {
                _errorMessage.value = "Error loading episode images: ${e.message}"
            }
        }
    }
}