package com.sol.tmdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    private val _selectedLanguage = MutableLiveData("en-US")
    val selectedLanguage: LiveData<String> = _selectedLanguage

    private val _isSearchBarVisible = MutableLiveData(false)
    val isSearchBarVisible: LiveData<Boolean> = _isSearchBarVisible

    fun changeLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun toggleSearchBar() {
        _isSearchBarVisible.value = _isSearchBarVisible.value?.not()
    }

}