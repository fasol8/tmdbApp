package com.sol.tmdb.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.SharedViewModel
import com.sol.tmdb.domain.model.movie.Certification
import com.sol.tmdb.domain.model.movie.CountryResult
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieImagesResponse
import com.sol.tmdb.domain.model.movie.MovieRecommendationResult
import com.sol.tmdb.domain.model.movie.MovieResult
import com.sol.tmdb.domain.model.movie.MovieSimilarResult
import com.sol.tmdb.domain.model.movie.MovieVideosResult
import com.sol.tmdb.domain.useCase.GetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
) :
    ViewModel() {

    //                          MutableStateFlow()
    private val _movies = MutableLiveData<List<MovieResult>>(emptyList())
    val movies: LiveData<List<MovieResult>> = _movies

    private val _nowPlaying = MutableLiveData<List<MovieResult>>(emptyList())
    val nowPlaying: LiveData<List<MovieResult>> = _nowPlaying

    private val _popularMovies = MutableLiveData<List<MovieResult>>(emptyList())
    val popularMovies: LiveData<List<MovieResult>> = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<MovieResult>>(emptyList())
    val topRatedMovies: LiveData<List<MovieResult>> = _topRatedMovies

    private val _upcomingMovies = MutableLiveData<List<MovieResult>>(emptyList())
    val upcomingMovies: LiveData<List<MovieResult>> = _upcomingMovies

    private val _movieById = MutableLiveData<MovieDetail?>()
    val movieById: LiveData<MovieDetail?> = _movieById

    private val _movieCertifications = MutableLiveData<Map<String?, Certification?>?>()
    val movieCertifications: LiveData<Map<String?, Certification?>?> = _movieCertifications

    private val _movieVideos = MutableLiveData<List<MovieVideosResult>>(emptyList())
    val movieVideos: LiveData<List<MovieVideosResult>> = _movieVideos

    private val _movieImages = MutableLiveData<MovieImagesResponse?>()
    val movieImages: LiveData<MovieImagesResponse?> = _movieImages

    private val _movieCredits = MutableLiveData<MovieCredits?>()
    val movieCredits: LiveData<MovieCredits?> = _movieCredits

    private val _movieProviders = MutableLiveData<Map<String, CountryResult?>>(emptyMap())
    val movieProviders: LiveData<Map<String, CountryResult?>> = _movieProviders

    private val _movieSimilar = MutableLiveData<List<MovieSimilarResult?>>(emptyList())
    val movieSimilar: LiveData<List<MovieSimilarResult?>> = _movieSimilar

    private val _movieRecommendation =
        MutableLiveData<List<MovieRecommendationResult?>>(emptyList())
    val movieRecommendation: LiveData<List<MovieRecommendationResult?>> = _movieRecommendation

    private val _language = MutableLiveData<String>("en-US")
    val language: LiveData<String> = _language

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1
    private var lastQuery: String? = null
    private var isLoading = false

    init {
        loadMovies()
    }

    fun observeLanguage(sharedViewModel: SharedViewModel, movieId: Int = 0) {
        sharedViewModel.selectedLanguage.observeForever { newLanguage ->
            _language.value = if (newLanguage == "es") "es-MX" else newLanguage
            currentPage = 1
            clearDataMovie()
            loadMoviesForLanguage(_language.value.toString())
            searchAllMovie(movieId, _language.value.toString())
        }
    }

    private fun clearDataMovie() {
        _movies.value = emptyList()
        _nowPlaying.value = emptyList()
        _popularMovies.value = emptyList()
        _topRatedMovies.value = emptyList()
        _upcomingMovies.value = emptyList()
    }

    private fun loadMoviesForLanguage(language: String) {
        loadMovies(language)
        loadNowPlaying(language)
        loadPopularMovies(language)
        loadTopRatedMovies(language)
        loadUpcomingMovies(language)
    }

    private fun searchAllMovie(movieId: Int, language: String) {
        searchMovieById(movieId, language)
        searchMovieCertification(movieId)
        searchMovieVideos(movieId, language)
        searchMovieImages(movieId)
        searchMovieCredits(movieId)
        searchMovieProvidersForMxAndUs(movieId)
        searchMovieSimilar(movieId)
        searchMovieRecommendation(movieId)
    }

    private fun <T> updateLiveData(liveData: MutableLiveData<List<T>>, newData: List<T>) {
        val updatedList = liveData.value.orEmpty() + newData
        liveData.value = updatedList
    }

    private fun <T, R> loadMoviesData(
        liveData: MutableLiveData<List<R>>,
        fetchMovies: suspend (Int) -> T,
        mapResponse: (T) -> List<R>
    ) {
        viewModelScope.launch {
            try {
                val response = fetchMovies(currentPage)
                val newMovies = mapResponse(response)
                if (newMovies.isNotEmpty()) {
                    updateLiveData(liveData, newMovies)
                    currentPage++
                } else {
                    _errorMessage.value = "No more movies"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun loadMovies(language: String = _language.toString()) {
        loadMoviesData(
            _movies,
            fetchMovies = { getMovieUseCase(currentPage, language) },
            mapResponse = { it.results })
    }

    fun loadNowPlaying(language: String = _language.value.toString()) {
        loadMoviesData(
            _nowPlaying,
            fetchMovies = { getMovieUseCase.getNowPlaying(currentPage, language) },
            mapResponse = { it.results }  // Mapeamos el resultado del tipo MovieNowResponse
        )
    }

    fun loadPopularMovies(language: String = _language.value.toString()) {
        loadMoviesData(
            _popularMovies,
            fetchMovies = {
                getMovieUseCase.getPopularMovie(currentPage, language)
            },
            mapResponse = { it.results }  // Mapeamos el resultado del tipo MovieResponse
        )
    }

    fun loadTopRatedMovies(language: String = _language.value.toString()) {
        loadMoviesData(_topRatedMovies,
            fetchMovies = { getMovieUseCase.getTopRated(currentPage, language) },
            mapResponse = { it.results }
        )
    }

    fun loadUpcomingMovies(language: String = _language.value.toString()) {
        loadMoviesData(
            _upcomingMovies,
            fetchMovies = { getMovieUseCase.getUpcoming(currentPage, language) },
            mapResponse = { it.results })
    }

    fun searchMovie(query: String) {
        if (isLoading) return
        viewModelScope.launch {
            try {
                isLoading = true
                if (query.isBlank()) {
                    loadMovies()
                } else {
                    if (query != lastQuery) {
                        lastQuery = query
                        currentPage = 1
                        _movies.value = emptyList()
                    }
                    val response = getMovieUseCase.getSearchMovie(query, currentPage)
                    val newMovies = response.results
                    if (newMovies.isNotEmpty()) {
                        val updateMovie = _movies.value.orEmpty() + newMovies
                        _movies.value = updateMovie
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

    private fun searchMovieById(id: Int, language: String) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieDetail(id, language)
                _movieById.value = response
            } catch (e: Exception) {
                _movieById.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchMovieCertification(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieReleaseWithCertification(movieId)
                _movieCertifications.value = response
            } catch (e: Exception) {
                _movieCertifications.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchMovieVideos(movieId: Int, language: String) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieVideos(movieId, language)
                _movieVideos.value = response.results
            } catch (e: Exception) {
                _movieVideos.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchMovieImages(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieImages(movieId)
                _movieImages.value = response
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"

            }
        }
    }

    private fun searchMovieCredits(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieCredits(movieId)
                _movieCredits.value = response
            } catch (e: Exception) {
                _movieCredits.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchMovieProvidersForMxAndUs(movieId: Int) {
        viewModelScope.launch {
            try {
                val result = getMovieUseCase.getProvidersForMxAndUsUseCase(movieId)
                _movieProviders.value = result
            } catch (e: Exception) {
                _errorMessage.value = "Error loading providers"
            }
        }
    }

    private fun searchMovieSimilar(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieSimilar(movieId)
                _movieSimilar.value = response.results
            } catch (e: Exception) {
                _movieSimilar.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    private fun searchMovieRecommendation(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieRecommendation(movieId)
                _movieRecommendation.value = response.results
            } catch (e: Exception) {
                _movieRecommendation.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}
