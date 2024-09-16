package com.sol.tmdb.navigation

sealed class TmdbScreen(val route: String, val title: String) {
    object MainScreen:TmdbScreen("mainScreen", "MainScreen")
    object Movie : TmdbScreen("movies", "Movies")
    object MovieDetail : TmdbScreen("movieDetail", "MovieDetail")
    object Tv : TmdbScreen("tvs", "Tvs")
    object TvDetail : TmdbScreen("tvDetail", "TvDetail")
    object TvSeason : TmdbScreen("tvSeason", "TvSeason")
    object Person : TmdbScreen("persons", "Persons")
    object PersonDetail : TmdbScreen("personDetail", "PersonDetail")
}