package com.sol.tmdb.presentation.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.movie.MovieCredits
import com.sol.tmdb.domain.model.movie.MovieDetail
import com.sol.tmdb.domain.model.movie.MovieResult
import com.sol.tmdb.domain.model.movie.MovieSimilarResult
import com.sol.tmdb.domain.useCase.GetMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val getMovieUseCase: GetMovieUseCase) :
    ViewModel() {

    //                          MutableStateFlow()
    private val _movies = MutableLiveData<List<MovieResult>>()
    val movies: LiveData<List<MovieResult>> = _movies

    private val _movieById = MutableLiveData<MovieDetail?>()
    val movieById: LiveData<MovieDetail?> = _movieById

    private val _movieCredits = MutableLiveData<MovieCredits?>()
    val movieCredits: LiveData<MovieCredits?> = _movieCredits

    private val _movieSimilar = MutableLiveData<List<MovieSimilarResult?>>()
    val movieSimilar: LiveData<List<MovieSimilarResult?>> = _movieSimilar

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase(currentPage)
                val newMovies = response.results
                if (newMovies.isNotEmpty()) {
                    val updateMovies = _movies.value.orEmpty() + newMovies
                    _movies.value = updateMovies
                    currentPage++
                } else {
                    _errorMessage.value = "NO more movies"
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchMovieById(id: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieDetail(id)
                _movieById.value = response
            } catch (e: Exception) {
                _movieById.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchMovieCredits(id: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieCredits(id)
                _movieCredits.value = response
            } catch (e: Exception) {
                _movieCredits.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }

    fun searchMovieSimilar(id: Int) {
        viewModelScope.launch {
            try {
                val response = getMovieUseCase.getMovieSimilar(id)
                _movieSimilar.value = response.results
            } catch (e: Exception) {
                _movieSimilar.value = emptyList()
                _errorMessage.value = "An error occurred: ${e.message}"
            }
        }
    }
}