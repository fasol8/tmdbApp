package com.sol.tmdb.domain.model.person

import com.sol.tmdb.R

enum class Gender(val value: Int, val genderResId: Int) {
    NOT_SPECIFIED(0, R.string.gender_not_specified),
    FEMALE(1, R.string.gender_female),
    MALE(2, R.string.gender_male),
    NON_BINARY(3, R.string.gender_non_binary);

    companion object {
        fun fromValue(value: Int): Gender {
            return when (value) {
                1 -> FEMALE
                2 -> MALE
                3 -> NON_BINARY
                else -> NOT_SPECIFIED
            }
        }
    }
}