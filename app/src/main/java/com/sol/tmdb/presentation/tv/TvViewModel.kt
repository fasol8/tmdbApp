package com.sol.tmdb.presentation.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.SharedViewModel
import com.sol.tmdb.domain.model.tv.CountryResult
import com.sol.tmdb.domain.model.tv.CreditsResponse
import com.sol.tmdb.domain.model.tv.SimilarResult
import com.sol.tmdb.domain.model.tv.TvCertification
import com.sol.tmdb.domain.model.tv.TvDetail
import com.sol.tmdb.domain.model.tv.TvImagesResponse
import com.sol.tmdb.domain.model.tv.TvImagesStill
import com.sol.tmdb.domain.model.tv.TvRecommendationsResult
import com.sol.tmdb.domain.model.tv.TvResponse
import com.sol.tmdb.domain.model.tv.TvResult
import com.sol.tmdb.domain.model.tv.TvSeasonDetailResponse
import com.sol.tmdb.domain.model.tv.TvVideosResult
import com.sol.tmdb.domain.useCase.GetTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction2

@HiltViewModel
class TvViewModel @Inject constructor(private val getTvUseCase: GetTvUseCase) : ViewModel() {

    private val _tvs = MutableLiveData<List<TvResult>>()
    val tvs: LiveData<List<TvResult>> = _tvs

    private val _airToday = MutableLiveData<List<TvResult>>()
    val airToday: LiveData<List<TvResult>> = _airToday

    private val _onAir = MutableLiveData<List<TvResult>>()
    val onAir: LiveData<List<TvResult>> = _onAir

    private val _popularTv = MutableLiveData<List<TvResult>>()
    val popularTv: LiveData<List<TvResult>> = _popularTv

    private val _topRatedTv = MutableLiveData<List<TvResult>>()
    val topRatedTv: LiveData<List<TvResult>> = _topRatedTv

    private val _tvById = MutableLiveData<TvDetail?>()
    val tvById: LiveData<TvDetail?> = _tvById

    private val _tvRatings = MutableLiveData<Map<String, TvCertification?>?>()
    val tvRatings: LiveData<Map<String, TvCertification?>?> = _tvRatings

    private val _tvCredits = MutableLiveData<CreditsResponse?>()
    val tvCredits: LiveData<CreditsResponse?> = _tvCredits

    private val _tvProviders = MutableLiveData<Map<String, CountryResult?>>()
    val tvProviders: LiveData<Map<String, CountryResult?>> = _tvProviders

    private val _tvImages = MutableLiveData<TvImagesResponse>()
    val tvImages: LiveData<TvImagesResponse> = _tvImages

    private val _tvVideos = MutableLiveData<List<TvVideosResult>>()
    val tvVideos: LiveData<List<TvVideosResult>> = _tvVideos

    private val _tvRecommendations = MutableLiveData<List<TvRecommendationsResult>>()
    val tvRecommendations: LiveData<List<TvRecommendationsResult>> = _tvRecommendations

    private val _tvSimilar = MutableLiveData<List<SimilarResult?>>()
    val tvSimilar: LiveData<List<SimilarResult?>> = _tvSimilar

    private val _seasonDetails = MutableLiveData<List<TvSeasonDetailResponse>>()
    val seasonDetails: LiveData<List<TvSeasonDetailResponse>> = _seasonDetails

    private val _episodeImages = MutableLiveData<Map<Int, List<TvImagesStill>>>()
    val episodeImages: LiveData<Map<Int, List<TvImagesStill>>> = _episodeImages

    private val _language = MutableLiveData<String>("en-US")
    val language: LiveData<String> = _language

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var currentPage = 1
    private var lastQuery: String? = null
    private var isLoading = false
    private val seasonDetailsList = mutableListOf<TvSeasonDetailResponse>()

    fun observeLanguage(sharedViewModel: SharedViewModel, tvId: Int = 0, numberOfSeasons: Int = 0) {
        sharedViewModel.selectedLanguage.observeForever { newLanguage ->
            _language.value = if (newLanguage == "es") "es-MX" else newLanguage
            currentPage = 1
            clearDataTv()
            loadTvsForLanguage(_language.value.toString())
            searchAllTv(tvId, _language.value.toString())
            loadAllSeasons(tvId, numberOfSeasons, _language.value.toString())
        }
    }

    private fun clearDataTv() {
        _tvs.value = emptyList()
        _airToday.value = emptyList()
        _onAir.value = emptyList()
        _popularTv.value = emptyList()
        _topRatedTv.value = emptyList()
        _seasonDetails.value= emptyList()
    }

    private fun loadTvsForLanguage(language: String) {
        loadTv(language)
        loadAirToday(language)
        loadOnAir(language)
        loadPopularTv(language)
        loadTopRatedTv(language)
    }

    fun searchAllTv(tvId: Int, language: String) {
        searchTvById(tvId, language)
        searchTvRatings(tvId)
        searchTvCredits(tvId)
        searchTvProvidersForMXAndUs(tvId)
        searchTvImages(tvId)
        searchTvVideos(tvId)
        searchTvSimilar(tvId)
        searchTvRecommendations(tvId)
    }

    private fun loadTvShows(
        loader: KSuspendFunction2<Int, String, TvResponse>,
        liveData: MutableLiveData<List<TvResult>>,
        language: String
    ) {
        viewModelScope.launch {
            try {
                val response = loader(currentPage, language)
                val newTvs = response.results.orEmpty()
                if (newTvs.isNotEmpty()) {
                    val updatedTvs = liveData.value.orEmpty() + newTvs
                    liveData.value = updatedTvs
                    currentPage++
                } else {
                    _errorMessage.value = "No more TV shows"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Network error: ${e.message}"
            } catch (e: HttpException) {
                _errorMessage.value = "API error: ${e.message}"
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun loadTv(language: String = _language.value.toString()) {
        loadTvShows(getTvUseCase::invoke, _tvs, language)
    }

    fun loadAirToday(language: String = _language.value.toString()) {
        loadTvShows(getTvUseCase::getAirToday, _airToday, language)
    }

    fun loadOnAir(language: String = _language.value.toString()) {
        loadTvShows(getTvUseCase::getOnAir, _onAir, language)
    }

    fun loadPopularTv(language: String = _language.value.toString()) {
        loadTvShows(getTvUseCase::getPopularTv, _popularTv, language)
    }

    fun loadTopRatedTv(language: String = _language.value.toString()) {
        loadTvShows(getTvUseCase::getTopRatedTv, _topRatedTv, language)
    }

    fun searchTv(query: String, language: String = _language.value.toString()) {
        if (isLoading) return
        viewModelScope.launch {
            try {
                isLoading = true
                if (query.isBlank()) {
                    loadTv(language)
                } else {
                    if (query != lastQuery) {
                        lastQuery = query
                        currentPage = 1
                        _tvs.value = emptyList()
                    }
                    val reponse = getTvUseCase.getSearchTv(query, currentPage)
                    val newTvs = reponse.results
                    if (newTvs.isNotEmpty()) {
                        val updateTvs = tvs.value.orEmpty() + newTvs
                        _tvs.value = updateTvs
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

    private fun searchTvById(tvId: Int, language: String) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvDetail(tvId, language)
                _tvById.value = response
            } catch (e: Exception) {
                _tvById.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchTvRatings(tvId: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvRatingsByCountry(tvId)
                _tvRatings.value = response
            } catch (e: Exception) {
                _tvRatings.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchTvCredits(tvId: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvCredits(tvId)
                _tvCredits.value = response
            } catch (e: Exception) {
                _tvCredits.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchTvProvidersForMXAndUs(tvId: Int) {
        viewModelScope.launch {
            try {
                val result = getTvUseCase.getTvProvidersForMxAndUsUseCase(tvId)
                _tvProviders.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Error loading providers"
            }
        }
    }

    private fun searchTvImages(tvId: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvImages(tvId)
                _tvImages.value = response
            } catch (e: Exception) {
                _errorMessage.value = "Error loading providers"
            }
        }
    }

    private fun searchTvVideos(tvId: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvVideos(tvId)
                _tvVideos.value = response.results.mapNotNull { it }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading providers"
            }
        }
    }

    private fun searchTvRecommendations(tvId: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTvRecommendations(tvId)
                _tvRecommendations.value = response.results.mapNotNull { it }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchTvSimilar(tvId: Int) {
        viewModelScope.launch {
            try {
                val response = getTvUseCase.getTVSimilar(tvId)
                _tvSimilar.value = response.results.mapNotNull { it }
            } catch (e: Exception) {
                _tvSimilar.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun loadAllSeasons(tvId: Int, numberOfSeasons: Int, language: String) {
        viewModelScope.launch {
            try {
                val seasonZero = getTvUseCase.getTvSeasonDetails(tvId, 0, language)
                seasonDetailsList.add(seasonZero)
                _seasonDetails.value = seasonDetailsList.toList()
            } catch (e: Exception) {
                _errorMessage.value = "Error loading season 0: ${e.message}"
            }
            for (seasonNumber in 1..numberOfSeasons) {
                try {
                    val response = getTvUseCase.getTvSeasonDetails(tvId, seasonNumber, language)
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
                    val response =
                        getTvUseCase.getTvImagesEpisode(tvId, seasonNumber, episodeNumber)
                    imagesMap[episodeNumber] = response.stills
                }
                _episodeImages.value = imagesMap
            } catch (e: Exception) {
                _errorMessage.value = "Error loading episode images: ${e.message}"
            }
        }
    }
}
