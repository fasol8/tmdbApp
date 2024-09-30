package com.sol.tmdb.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sol.tmdb.utils.SharedViewModel
import com.sol.tmdb.presentation.main.MainListScreen
import com.sol.tmdb.presentation.movie.MovieDetail
import com.sol.tmdb.presentation.movie.MoviesScreen
import com.sol.tmdb.presentation.person.PersonDetail
import com.sol.tmdb.presentation.person.PersonScreen
import com.sol.tmdb.presentation.tv.TvDetail
import com.sol.tmdb.presentation.tv.TvScreen
import com.sol.tmdb.presentation.tv.TvSeason

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TmdbNavHost(navController: NavHostController, sharedViewModel: SharedViewModel) {
    NavHost(navController = navController, startDestination = TmdbScreen.MainScreen.route) {
        composable(TmdbScreen.MainScreen.route) { MainListScreen(navController = navController) }

        composable(TmdbScreen.Movie.route) { MoviesScreen("movie", navController, sharedViewModel) }
        composable(TmdbScreen.NowPlaying.route) { MoviesScreen("now_playing", navController, sharedViewModel) }
        composable(TmdbScreen.PopularMovies.route) { MoviesScreen("popular", navController, sharedViewModel) }
        composable(TmdbScreen.TopRatedMovies.route) { MoviesScreen("top_rated", navController, sharedViewModel) }
        composable(TmdbScreen.UpcomingMovies.route) { MoviesScreen("upcoming", navController, sharedViewModel) }
        composable(
            route = TmdbScreen.MovieDetail.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val movieId = navBackStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetail(movieId = movieId, navController = navController, sharedViewModel)
        }

        composable(TmdbScreen.Tv.route) { TvScreen("tv", navController, sharedViewModel) }
        composable(TmdbScreen.AirToday.route) { TvScreen("air_today", navController, sharedViewModel) }
        composable(TmdbScreen.OnAir.route) { TvScreen("on_the_air", navController, sharedViewModel) }
        composable(TmdbScreen.PopularTv.route) { TvScreen("popular_tv", navController, sharedViewModel) }
        composable(TmdbScreen.TopRatedTv.route) { TvScreen("top_rated_tv", navController, sharedViewModel) }
        composable(
            route = TmdbScreen.TvDetail.route + "/{tvId}",
            arguments = listOf(navArgument("tvId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val tvId = navBackStackEntry.arguments?.getInt("tvId") ?: return@composable
            TvDetail(tvId = tvId, navController, sharedViewModel)
        }
        composable(
            route = TmdbScreen.TvSeason.route + "/{tvSeasons}/{numberOfSeasons}",
            arguments = listOf(
                navArgument("tvSeasons") { type = NavType.IntType },
                navArgument("numberOfSeasons") { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            val tvId = navBackStackEntry.arguments?.getInt("tvSeasons") ?: return@composable
            val numberOfSeasons =
                navBackStackEntry.arguments?.getInt("numberOfSeasons") ?: return@composable
            TvSeason(tvId, numberOfSeasons, navController, sharedViewModel)
        }

        composable(TmdbScreen.Person.route) { PersonScreen(navController, sharedViewModel) }
        composable(
            TmdbScreen.PersonDetail.route + "/{personId}",
            arguments = listOf(navArgument("personId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val personId = navBackStackEntry.arguments?.getInt("personId") ?: return@composable
            PersonDetail(personId = personId, navController, sharedViewModel)
        }
    }
}