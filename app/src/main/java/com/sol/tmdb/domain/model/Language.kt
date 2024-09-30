package com.sol.tmdb.domain.model

import androidx.annotation.DrawableRes

data class Language(
    val code: String,
    @DrawableRes val flag: Int
)