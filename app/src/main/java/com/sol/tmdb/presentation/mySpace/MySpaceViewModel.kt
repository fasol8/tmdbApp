package com.sol.tmdb.presentation.mySpace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.data.repository.db.movie.MovieEntity
import com.sol.tmdb.data.repository.db.tv.TvEntity
import com.sol.tmdb.domain.useCase.GetMovieUseCase
import com.sol.tmdb.domain.useCase.GetTvUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySpaceViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getTvUseCase: GetTvUseCase,
) : ViewModel() {

    private val _moviesFav = MutableLiveData<List<MovieEntity>>()
    val moviesFav: LiveData<List<MovieEntity>> = _moviesFav

    private val _moviesWatch = MutableLiveData<List<MovieEntity>>()
    val moviesWatch: LiveData<List<MovieEntity>> = _moviesWatch

    private val _tvsFav = MutableLiveData<List<TvEntity>>()
    val tvsFav: LiveData<List<TvEntity>> = _tvsFav

    private val _tvsWatch = MutableLiveData<List<TvEntity>>()
    val tvsWatch: LiveData<List<TvEntity>> = _tvsWatch

    fun loadAll() {
        loadFavoriteMovies()
        loadWatchListMovies()
        loadFavoriteTvs()
        loadWatchListTvs()
    }

    private fun loadFavoriteMovies() {
        viewModelScope.launch {
            val favorites = getMovieUseCase.getFavoriteMovies()
            _moviesFav.postValue(favorites)
        }
    }

    private fun loadWatchListMovies() {
        viewModelScope.launch {
            val watchList = getMovieUseCase.getWatchListMovies()
            _moviesWatch.postValue(watchList)
        }
    }

    private fun loadFavoriteTvs() {
        viewModelScope.launch {
            val favorites = getTvUseCase.getFavoriteTvs()
            _tvsFav.postValue(favorites)
        }
    }

    private fun loadWatchListTvs() {
        viewModelScope.launch {
            val watchList = getTvUseCase.getWatchListTvs()
            _tvsWatch.postValue(watchList)
        }
    }
}