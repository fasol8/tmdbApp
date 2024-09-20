package com.sol.tmdb.navigation

sealed class TmdbScreen(val route: String, val title: String) {
    object MainScreen:TmdbScreen("mainScreen", "MainScreen")

    object Movie : TmdbScreen("movies", "Movies")
    object NowPlaying : TmdbScreen("now_playing", "Now Playing")
    object PopularMovies : TmdbScreen("popular_movies", "Popular Movies")
    object TopRatedMovies : TmdbScreen("top_rated_movies", "Top Rated Movies")
    object UpcomingMovies : TmdbScreen("upcoming_movies", "Upcoming Movies")
    object MovieDetail : TmdbScreen("movieDetail", "MovieDetail")

    object Tv : TmdbScreen("tvs", "Tvs")
    object AirToday : TmdbScreen("air_today", "Air Today")
    object OnAir : TmdbScreen("on_the_air", "On Air")
    object PopularTv : TmdbScreen("popular_tv", "Popular Tv")
    object TopRatedTv : TmdbScreen("top_rated_tv", "Top Rated Tv")
    object TvDetail : TmdbScreen("tvDetail", "TvDetail")
    object TvSeason : TmdbScreen("tvSeason", "TvSeason")

    object Person : TmdbScreen("persons", "Persons")
    object PersonDetail : TmdbScreen("personDetail", "PersonDetail")
}