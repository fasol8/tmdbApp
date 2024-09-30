package com.sol.tmdb.domain.model.movie

import android.content.Context
import com.sol.tmdb.R

enum class MovieGenre(val id: Int, val genreResId: Int) {
    ACTION(28, R.string.action),
    ADVENTURE(12, R.string.adventure),
    ANIMATION(16, R.string.animation),
    COMEDY(35, R.string.comedy),
    CRIME(80, R.string.crime),
    DOCUMENTARY(99, R.string.documentary),
    DRAMA(18, R.string.drama),
    FAMILY(10751, R.string.family),
    FANTASY(14, R.string.fantasy),
    HISTORY(36, R.string.history),
    HORROR(27, R.string.horror),
    MUSIC(10402, R.string.music),
    MYSTERY(9648, R.string.mystery),
    ROMANCE(10749, R.string.romance),
    SCIENCE_FICTION(878, R.string.science_fiction),
    TV_MOVIE(10770, R.string.tv_movie),
    THRILLER(53, R.string.thriller),
    WAR(10752, R.string.war),
    WESTERN(37, R.string.western);

    fun getGenreName(context: Context): String {
        return context.getString(genreResId)
    }

    companion object {
        fun fromId(id: Int): MovieGenre? {
            return values().find { it.id == id }
        }
    }
}
