package com.sol.tmdb.domain.model.person

enum class Gender(val value: Int) {
    NOT_SPECIFIED(0),
    FEMALE(1),
    MALE(2),
    NON_BINARY(3);

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