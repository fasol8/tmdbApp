package com.sol.tmdb.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.tmdb.domain.model.main.TrendingResult
import com.sol.tmdb.domain.useCase.GetMainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getMainUseCase: GetMainUseCase) : ViewModel() {

    private val _trending = MutableLiveData<List<TrendingResult>>()
    val trending: LiveData<List<TrendingResult>> = _trending

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadTrending("all", "day")
    }

    private fun loadTrending(type: String, time: String) {
        viewModelScope.launch {
            try {
                val response = getMainUseCase(type, time)
                _trending.value = response.results
                Log.i("VM", _trending.value.toString())
            } catch (e: Exception) {
//                _trending.value = null
                _errorMessage.value = "An error occurred: ${e.message}"
                Log.i("Error", e.message.toString())
            }
        }
    }
}