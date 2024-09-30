package com.sol.tmdb.navigation

import com.sol.tmdb.R

sealed class TmdbScreen(val route: String, val titleResId: Int) {
    object MainScreen : TmdbScreen("mainScreen", R.string.mainscreen_title)

    object Movie : TmdbScreen("movies", R.string.movies_title)
    object NowPlaying : TmdbScreen("now_playing", R.string.now_playing_title)
    object PopularMovies : TmdbScreen("popular_movies", R.string.popular_title)
    object TopRatedMovies : TmdbScreen("top_rated_movies", R.string.top_rated_movies_title)
    object UpcomingMovies : TmdbScreen("upcoming_movies", R.string.upcoming_title)
    object MovieDetail : TmdbScreen("movieDetail", R.string.movies_detail_title)

    object Tv : TmdbScreen("tvs", R.string.tvs_title)
    object AirToday : TmdbScreen("air_today", R.string.airing_tv_title)
    object OnAir : TmdbScreen("on_the_air", R.string.on_the_air_title)
    object PopularTv : TmdbScreen("popular_tv", R.string.popular_tv_title)
    object TopRatedTv : TmdbScreen("top_rated_tv", R.string.top_rated_tv_title)
    object TvDetail : TmdbScreen("tvDetail", R.string.tv_detail_title)
    object TvSeason : TmdbScreen("tvSeason", R.string.tv_season_detail)

    object Person : TmdbScreen("persons", R.string.person_title)
    object PersonDetail : TmdbScreen("personDetail", R.string.person_detail_title)
}